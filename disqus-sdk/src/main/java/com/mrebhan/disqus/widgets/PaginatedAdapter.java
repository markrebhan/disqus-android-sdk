package com.mrebhan.disqus.widgets;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.fragment.ViewHolderType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *  Base adapter implementation for adapters associated with {@link com.mrebhan.disqus.datamodel.PaginatedList}
 */
public abstract class PaginatedAdapter<T extends Parcelable> extends RecyclerView.Adapter implements Callback<PaginatedList<T>>{
    private static final String ARG_PAGINATED_LISTS = ".paginatedLists";
    private static final String ARG_ALL_RESOURCE_ITEMS = ".allResourceItems";
    private static final String ARG_POSITION_META_DATA = ".positionMetaData";

    private ArrayList<PaginatedList<T>> paginatedLists = new ArrayList<>(); // all paginated lists that have been fetched
    private ArrayList<T> allResourceItems = new ArrayList<>();
    private ArrayList<PositionMetaData> positionMetaData = new ArrayList<>();

    private AtomicBoolean loading = new AtomicBoolean(false);

    protected abstract void loadNextPage(String cursorId, Callback<PaginatedList<T>> callback);

    public void onSaveInstanceState(String prefix, Bundle outState) {
        outState.putParcelableArrayList(prefix + ARG_PAGINATED_LISTS, paginatedLists);
        outState.putParcelableArrayList(prefix + ARG_ALL_RESOURCE_ITEMS, allResourceItems);
        outState.putParcelableArrayList(prefix + ARG_POSITION_META_DATA, positionMetaData);
    }

    public void onRestoreInstanceState(String prefix, Bundle savedInstanceState) {
        paginatedLists = savedInstanceState.getParcelableArrayList(prefix + ARG_PAGINATED_LISTS);
        allResourceItems = savedInstanceState.getParcelableArrayList(prefix + ARG_ALL_RESOURCE_ITEMS);
        positionMetaData = savedInstanceState.getParcelableArrayList(prefix + ARG_POSITION_META_DATA);
    }

    public void addList(PaginatedList<T> list) {
        if (list != null) {
            paginatedLists.add(list);
            allResourceItems.addAll(list.getResponseData());
            for (T item: list.getResponseData()) {
                // TODO figure out reply logic and margining for it
                positionMetaData.add(new PositionMetaData(ViewHolderType.COMMENT, allResourceItems.indexOf(item), 0));
            }
        }
    }

    public void addViewHolderType(ViewHolderType type, int position) {
        addViewHolderType(type, position, 0);
    }

    public void addViewHolderType(ViewHolderType type, int position, int viewOffset) {
        positionMetaData.add(position, new PositionMetaData(type, -1, viewOffset));
        notifyItemInserted(position);
    }

    public void removeViewHolderType(int position) {
        positionMetaData.remove(position);
        notifyItemRemoved(position);
    }

    protected int getLeftPixelOffset(int position) {
        return positionMetaData.size() > position  ? positionMetaData.get(position).viewPixelOffset : -1;
    }

    public void loadNext() {
        Cursor cursor = paginatedLists.get(paginatedLists.size() - 1).getCursor();
        if (cursor.hasNext()) {
            if (loading.compareAndSet(false, true)) {
                loadNextPage(cursor.getNextPage(), this);
            } else {
                Log.d("","Already loading next page");
            }
        }
    }

    protected Object getItem(int position) {
        int index = positionMetaData.get(position).dataPosition;
        if (index != -1)  {
            return allResourceItems.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return positionMetaData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 10) {
            loadNext();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return positionMetaData.get(position).viewHolderType.ordinal();
    }

    /**
     * Callback implementation for loading subsequent pages
     */

    @Override
    public void success(PaginatedList<T> paginatedList, Response response) {
        addList(paginatedList);
        notifyDataSetChanged();
        loading.set(false);
    }

    @Override
    public void failure(RetrofitError error) {
        loading.set(false);
    }

    /**
     * Private inner class stores all positional meta data describing how the view holder should set
     * up the item when bound.
     */
    private static class PositionMetaData implements Parcelable {
        private ViewHolderType viewHolderType;
        private int dataPosition;
        private int viewPixelOffset;

        private PositionMetaData(ViewHolderType viewHolderType, int dataPosition, int viewPixelOffset) {
            this.viewHolderType = viewHolderType;
            this.dataPosition = dataPosition;
            this.viewPixelOffset = viewPixelOffset;
        }

        /**
         * Empty constructor required for parcelable
         */
        public PositionMetaData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewHolderType == null ? -1 : this.viewHolderType.ordinal());
            dest.writeInt(this.dataPosition);
            dest.writeInt(this.viewPixelOffset);
        }

        private PositionMetaData(Parcel in) {
            int tmpViewHolder = in.readInt();
            this.viewHolderType = tmpViewHolder == -1 ? null : ViewHolderType.values()[tmpViewHolder];
            this.dataPosition = in.readInt();
            this.viewPixelOffset = in.readInt();
        }

        public static final Creator<PositionMetaData> CREATOR = new Creator<PositionMetaData>() {
            public PositionMetaData createFromParcel(Parcel source) {
                return new PositionMetaData(source);
            }

            public PositionMetaData[] newArray(int size) {
                return new PositionMetaData[size];
            }
        };
    }
}
