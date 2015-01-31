package com.mrebhan.disqus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;
import com.mrebhan.disqus.widgets.PaginatedAdapter;

import javax.inject.Inject;

import retrofit.Callback;

public class PostsAdapter extends PaginatedAdapter<Post> {

    @Inject
    ThreadPostsService threadPostsService;

    private MyMenuClickListener listener = new MyMenuClickListener();

    @Inject
    public PostsAdapter() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

    @Override
    protected void loadNextPage(String cursorId, Callback<PaginatedList<Post>> callback) {
        threadPostsService.getNextPage(cursorId, DisqusSdkProvider.publicKey, callback);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderType viewHolderType = ViewHolderType.values()[viewType];
        switch (viewHolderType) {
            case COMMENT:
                return CommentItem.createInstance(parent, listener);
            case ACTION_BAR:
                return ActionBarItem.createInstance(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ViewHolderItem) holder).onBindViewHolder(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null) {
            return ViewHolderType.COMMENT.ordinal();
        } else {
            return ViewHolderType.ACTION_BAR.ordinal();
        }
    }

    private class MyMenuClickListener implements CommentItem.Listener {
        @Override
        public void onActionMoreClicked(int position, boolean isOpen) {
            if (isOpen) {
                addViewHolderType(ViewHolderType.ACTION_BAR, position + 1); // add item below the comment item
            } else {
                removeViewHolderType(position + 1); // remove item below the comment item
            }
        }
    }
}
