package com.mrebhan.disqus.widgets;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;

import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.fragment.ViewHolderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *  Base adapter implementation for adapters associated with {@link com.mrebhan.disqus.datamodel.PaginatedList}
 */
public abstract class PaginatedAdapter<T extends Parcelable> extends RecyclerView.Adapter implements Callback<PaginatedList<T>>{
    private static final String ARG_PAGINATED_LISTS = ".paginatedList";
    private static final String ARG_ALL_ADAPTER_ITEMS = ".allAdapterItems";
    private static final String ARG_VIEW_PIXEL_OFFSETS = ".viewPixesOffsets";

    private ArrayList<PaginatedList<T>> paginatedLists = new ArrayList<>(); // all paginated lists that have been fetched
    private ArrayList<T> allResourceItems = new ArrayList<>(); // All individual items retrieved
    //FIXME put the nex three lists into a tuple since they are all same length and describes view at each position
    //FIXME add ability for comment view to have offsets properly
    private ArrayList<ViewHolderType> allAdapterItems = new ArrayList<>();
    private List<Integer> dataPositions = new LinkedList<>(); // keep track of positions of entity in adapter as other new items are added in the all adapter list
    private ArrayList<Integer> viewPixelOffsets = new ArrayList<>();
    private int count = 0;

    private AtomicBoolean loading = new AtomicBoolean(false);

    protected abstract void loadNextPage(String cursorId, Callback<PaginatedList<T>> callback);

    public void onSaveInstanceState(String prefix, Bundle outState) {
        outState.putParcelableArrayList(prefix + ARG_PAGINATED_LISTS, paginatedLists);

        int[] allAdapterItemOrdinals = new int[allAdapterItems.size()];
        for (int i = 0; i < allAdapterItemOrdinals.length; i++) {
            allAdapterItemOrdinals[i] = allAdapterItems.get(i).ordinal();
        }
        outState.putIntArray(prefix + ARG_ALL_ADAPTER_ITEMS, allAdapterItemOrdinals);
        outState.putIntegerArrayList(prefix + ARG_VIEW_PIXEL_OFFSETS, viewPixelOffsets);
    }

    public void onRestoreInstanceState(String prefix, Bundle savedInstanceState) {
        ArrayList<PaginatedList<T>> list = savedInstanceState.getParcelableArrayList(prefix + ARG_PAGINATED_LISTS);
        if (list != null) {
            for (PaginatedList<T> item : list) {
                addList(item);
            }
            notifyDataSetChanged();
        }

        int[] allAdapterItemsOrdinals = savedInstanceState.getIntArray(prefix + ARG_ALL_ADAPTER_ITEMS);
        ArrayList<Integer> integers = savedInstanceState.getIntegerArrayList(prefix + ARG_VIEW_PIXEL_OFFSETS);

        for (int i = 0; i < allAdapterItemsOrdinals.length; i++) {
            ViewHolderType viewHolderType = ViewHolderType.values()[allAdapterItemsOrdinals[i]];
            if (viewHolderType != ViewHolderType.COMMENT) {
                int offset = integers.get(i);
                addViewHolderType(viewHolderType, i, offset);
            }
        }
    }

    public void addList(PaginatedList<T> list) {
        if (list != null) {
            paginatedLists.add(list);
            allResourceItems.addAll(list.getResponseData());
            count += list.getResponseData().size();
            for (T item: list.getResponseData()) {
                dataPositions.add(allResourceItems.indexOf(item));
                allAdapterItems.add(ViewHolderType.COMMENT);
                viewPixelOffsets.add(0);
            }
        }
    }

    public void addViewHolderType(ViewHolderType type, int position) {
        addViewHolderType(type, position, 0);
    }

    public void addViewHolderType(ViewHolderType type, int position, int viewOffset) {
        allAdapterItems.add(position, type);
        dataPositions.add(position, -1);
        viewPixelOffsets.add(position, viewOffset);
        count++;
        notifyItemInserted(position);
    }

    public void removeViewHolderType(int position) {
        allAdapterItems.remove(position);
        dataPositions.remove(position);
        viewPixelOffsets.remove(position);
        count--;
        notifyItemRemoved(position);
    }

    protected int getLeftPixelOffset(int position) {
        return viewPixelOffsets.size() > position && viewPixelOffsets.get(position) != null ? viewPixelOffsets.get(position) : -1;
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
        int index = dataPositions.get(position);
        if (index != -1)  {
            return allResourceItems.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == count - 10) {
            loadNext();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return allAdapterItems.get(position).ordinal();
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
}
