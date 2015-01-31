package com.mrebhan.disqus.widgets;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.fragment.ViewHolderType;

import java.util.ArrayList;
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

    private ArrayList<PaginatedList<T>> paginatedLists = new ArrayList<>(); // all paginated lists that have been fetched
    private ArrayList<T> allResourceItems = new ArrayList<>(); // All individual items retrieved
    private ArrayList<ViewHolderType> allAdapterItems = new ArrayList<>();
    private List<Integer> dataPositions = new LinkedList<>(); // keep track of positions of entity in adapter as other new items are added in the all adapter list
    private int count = 0;

    private AtomicBoolean loading = new AtomicBoolean(false);

    protected abstract void loadNextPage(String cursorId, Callback<PaginatedList<T>> callback);

    public void onSaveInstanceState(String prefix, Bundle outState) {
        outState.putParcelableArrayList(prefix, paginatedLists);
    }

    public void onRestoreInstanceState(String prefix, Bundle savedInstanceState){
        ArrayList<PaginatedList<T>> list = savedInstanceState.getParcelableArrayList(prefix);
        if (list != null) {
            for (PaginatedList<T> item : list) {
                addList(item);
            }
            notifyDataSetChanged();
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
            }
        }
    }

    public void addViewHolderType(ViewHolderType type) {
        allAdapterItems.add(type);
        count++;
        dataPositions.add(-1);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addViewHolderType(ViewHolderType type, int position) {
        allAdapterItems.add(position, type);
        count++;
        dataPositions.add(position, -1);
        notifyItemInserted(position);

    }

    public void removeViewHolderType(int position) {
        allAdapterItems.remove(position);
        count--;
        dataPositions.remove(position);
        notifyItemRemoved(position);
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
