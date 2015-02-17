package com.mrebhan.disqus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrebhan.disqus.DisqusSdkDaggerModule;
import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;

public class PostCommentItem extends RecyclerView.ViewHolder implements ViewHolderItem {

    public PostCommentItem(View itemView) {
        super(itemView);
    }

    public static PostCommentItem createInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_entry, parent, false);
        return new PostCommentItem(view);
    }

    @Override
    public ViewHolderType getViewItemType() {
        return ViewHolderType.POST_COMMENT;
    }

    @Override
    public void onBindViewHolder(Object data, int leftPixelOffset) {

    }

    @Override
    public void injectThis() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }
}
