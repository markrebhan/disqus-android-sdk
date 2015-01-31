package com.mrebhan.disqus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;

public class ActionBarItem extends RecyclerView.ViewHolder implements ViewHolderItem {
    public TextView moreShare;
    public TextView moreReply;
    public ImageView moreMinimize;
    public ImageView moreFlag;

    public ActionBarItem(View itemView) {
        super(itemView);
        moreShare = (TextView) itemView.findViewById(R.id.more_share);
        moreReply = (TextView) itemView.findViewById(R.id.more_reply);
        moreMinimize = (ImageView) itemView.findViewById(R.id.more_minimize);
        moreFlag = (ImageView) itemView.findViewById(R.id.more_flag);
    }

    public static ActionBarItem createInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_more, parent, false);
        return new ActionBarItem(view);
    }

    @Override
    public ViewHolderType getViewItemType() {
        return ViewHolderType.ACTION_BAR;
    }

    @Override
    public void onBindViewHolder(Object data) {

    }

    @Override
    public void injectThis() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }
}
