package com.mrebhan.disqus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mrebhan.disqus.datamodel.Entity;

/**
 * Common interface for view holder items to implement
 */
public interface ViewHolderItem {

    public ViewHolderType getViewItemType();
    public void onBindViewHolder(Object data);
    public void injectThis();

}
