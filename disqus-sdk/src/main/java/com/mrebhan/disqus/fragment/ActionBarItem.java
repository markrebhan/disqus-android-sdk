package com.mrebhan.disqus.fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;

import javax.inject.Inject;

public class ActionBarItem extends RecyclerView.ViewHolder implements ViewHolderItem {

    @Inject
    Context appContext;

    public TextView moreShare;
    public TextView moreReply;
    public ImageView moreMinimize;
    public ImageView moreFlag;
    public CardView cardView;

    public ActionBarItem(View itemView) {
        super(itemView);
        injectThis();
        moreShare = (TextView) itemView.findViewById(R.id.more_share);
        moreReply = (TextView) itemView.findViewById(R.id.more_reply);
        moreMinimize = (ImageView) itemView.findViewById(R.id.more_minimize);
        moreFlag = (ImageView) itemView.findViewById(R.id.more_flag);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
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
    public void onBindViewHolder(Object data, int leftPixelOffset) {

        // calculate the appropriate width for the new item and set layout params of parent view
        // ensure that the layout gravity is set to right on the parent view.
        if (leftPixelOffset != -1) {
            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
            layoutParams.width = appContext.getResources().getDisplayMetrics().widthPixels - leftPixelOffset;
            cardView.setLayoutParams(layoutParams);
            Log.d("", "new width is " + Integer.toString(layoutParams.width));
        }
    }

    @Override
    public void injectThis() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

}
