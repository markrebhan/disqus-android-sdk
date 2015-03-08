package com.mrebhan.disqus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;
import com.mrebhan.disqus.widgets.PaginatedAdapter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;

public class PostsAdapter extends PaginatedAdapter<Post> {

    @Inject
    ThreadPostsService threadPostsService;

    private List<Integer> viewPixelOffsets = new LinkedList<>();
    private MyMenuClickListener listener = new MyMenuClickListener();

    @Inject
    public PostsAdapter() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

    @Override
    protected void loadNextPage(String cursorId, Callback<PaginatedList<Post>> callback) {
        threadPostsService.getNextPage(cursorId, callback);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderType viewHolderType = ViewHolderType.values()[viewType];
        switch (viewHolderType) {
            case COMMENT:
                return CommentItem.createInstance(parent, listener);
            case ACTION_BAR:
                return ActionBarItem.createInstance(parent);
            case POST_COMMENT:
                return PostCommentItem.createInstance(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ViewHolderItem) holder).onBindViewHolder(getItem(position), getLeftPixelOffset(position));
    }

    protected void addPostCommentRow() {
        if (getItemViewType(0) != ViewHolderType.POST_COMMENT.ordinal()) {
            addViewHolderType(ViewHolderType.POST_COMMENT, 0);
        }
    }

    protected void removePostCommentRow() {
        if (getItemViewType(0) == ViewHolderType.POST_COMMENT.ordinal()) {
            removeViewHolderType(0);
        }
    }

    private class MyMenuClickListener implements CommentItem.Listener {
        /**
         * if the next item in the list is not an action row, then add one, else delete it.
         */
        @Override
        public void onActionMoreClicked(int position, int leftViewPosition) {
            if (getItemCount() == position + 1 || getItemViewType(position + 1) != ViewHolderType.ACTION_BAR.ordinal()) {
                addViewHolderType(ViewHolderType.ACTION_BAR, position + 1, leftViewPosition); // add item below the comment item if not already there
            } else {
                removeViewHolderType(position + 1); // remove item below the comment item
            }
        }

        /**
         * Check to see if the next row in the adapter is action row.
         */
        @Override
        public boolean isActionBarOpen(int position) {
            return getItemCount() > position + 1 && getItemViewType(position + 1) == ViewHolderType.ACTION_BAR.ordinal();
        }
    }

}
