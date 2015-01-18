package com.mrebhan.disqus.widgets;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.datamodel.PaginatedList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *  Base adapter implementation for adapters associated with {@link com.mrebhan.disqus.datamodel.PaginatedList}
 */
public abstract class PaginatedAdapter<T extends Parcelable, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Callback<PaginatedList<T>>{

    private ArrayList<PaginatedList<T>> paginatedLists = new ArrayList<>(); // all paginated lists that have been fetched
    private ArrayList<T> allResourceItems = new ArrayList<>(); // All individual items retrieved
    private ArrayList<Integer> paginatedListCounts = new ArrayList<>(); // total number of items after each entry
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
            paginatedListCounts.add(count);
        }

    }

    public void loadNext() {
        Cursor cursor = paginatedLists.get(paginatedLists.size() - 1).getCursor();
        if (cursor.hasNext()) {
            loading.compareAndSet(false, true);
            loadNextPage(cursor.getNextPage(), this);
        }
    }

    protected T getItem(int position) {
        return allResourceItems.get(position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
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
