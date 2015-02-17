package com.mrebhan.disqus.fragment;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkDaggerModule;
import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.datamodel.Avatar;
import com.mrebhan.disqus.datamodel.Post;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

public class CommentItem extends RecyclerView.ViewHolder implements ViewHolderItem {

    @Inject
    Picasso picasso;
    @Inject
    Context context;

    public ImageView profileImage;
    public TextView username;
    public TextView replyUsername;
    public TextView timeAgo;
    public TextView comment;
    public TextView upVotes;
    public View upVoteContainer;
    public View downVoteContainer;
    public View subMenu;
    public ImageView subMenuImage;
    private Listener listener;
    private int leftOffset;

    public CommentItem(View itemView, Listener listener) {
        super(itemView);
        injectThis();
        profileImage = (ImageView) itemView.findViewById(R.id.img_avatar);
        username = (TextView) itemView.findViewById(R.id.txt_user_name);
        replyUsername = (TextView) itemView.findViewById(R.id.txt_reply_user);
        timeAgo = (TextView) itemView.findViewById(R.id.txt_time_ago);
        comment = (TextView) itemView.findViewById(R.id.txt_comment);
        upVotes = (TextView) itemView.findViewById(R.id.txt_up_votes);
        upVoteContainer = itemView.findViewById(R.id.frame_up_vote);
        downVoteContainer = itemView.findViewById(R.id.frame_down_vote);
        subMenu = itemView.findViewById(R.id.post_menu);
        subMenuImage = (ImageView) itemView.findViewById(R.id.post_menu_image);
        this.listener = listener;
    }

    public static CommentItem createInstance(ViewGroup parent, Listener listener) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new CommentItem(view, listener);
    }

    @Override
    public ViewHolderType getViewItemType() {
        return ViewHolderType.COMMENT;
    }

    @Override
    public void onBindViewHolder(Object data, int leftPixelOffset) {
        Post currentPost = (Post) data;

        username.setText(currentPost.getAuthor().getUsername());
        comment.setText(currentPost.getRawMessage());
        upVotes.setText(Integer.toString(currentPost.getLikes()));
        timeAgo.setText(buildTimeAgo(currentPost.getCreatedDate()));

        Avatar avatar = currentPost.getAuthor().getAvatar();
        if (avatar.getPermalinkUrl() != null) {
            picasso.load(avatar.getPermalinkUrl()).error(R.drawable.no_avatar_92).into(profileImage);
        } else {
            picasso.load(R.drawable.no_avatar_92).into(profileImage);
        }

        subMenu.setOnClickListener(new MyMenuClickListener());

        // of the action bar is open, then we must rotate the image 90 degrees
        if (listener.isActionBarOpen(getPosition())) {
            subMenuImage.setRotation(90f);
        }

    }


    @Override
    public void injectThis() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

    private String buildTimeAgo(Date createDate) {
        Calendar now = new GregorianCalendar();
        Calendar create = new GregorianCalendar();
        create.setTime(createDate);

        int nowYear = now.get(Calendar.YEAR);
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int nowHour = now.get(Calendar.HOUR);
        int nowMinute = now.get(Calendar.MINUTE);

        int createYear = create.get(Calendar.YEAR);
        int createDay = create.get(Calendar.DAY_OF_YEAR);
        int createHour = create.get(Calendar.HOUR);
        int createMinute = create.get(Calendar.MINUTE);

        if (nowYear == createYear && nowDay == createDay && nowHour == createHour && nowMinute == createMinute) {
            return context.getString(R.string.moments_ago);
        } else if (nowYear == createYear && nowDay == createDay && nowHour == createHour) {
            int diff = nowMinute - createMinute;
            return diff == 1 ? context.getString(R.string.minute_ago) : String.format(context.getString(R.string.minutes_ago), diff);
        } else if (nowYear == createYear && nowDay == createDay) {
            if (nowHour - 1 == createHour && nowMinute < createMinute) {
                int diff = nowMinute + (60 - createMinute);
                return diff == 1 ? context.getString(R.string.minute_ago) : String.format(context.getString(R.string.minutes_ago), diff);
            } else {
                int diff = nowHour - createHour;
                return diff == 1 ? context.getString(R.string.hour_ago) : String.format(context.getString(R.string.hours_ago), diff);
            }
        } else if (nowYear == createYear) {
            if (nowDay - 1 == createDay && nowHour < createHour) {
                int diff = nowHour + (24 - createHour);
                return diff == 1 ? context.getString(R.string.hour_ago) : String.format(context.getString(R.string.hours_ago), diff);
            } else {
                int diff = nowDay - createDay;
                return diff == 1 ? context.getString(R.string.day_ago) : String.format(context.getString(R.string.days_ago), diff);
            }
        } else {
            if (nowYear - 1 == createYear && nowDay < createDay) {
                int diff = nowDay + (365 - createDay);
                return diff == 1 ? context.getString(R.string.day_ago) : String.format(context.getString(R.string.days_ago), diff);
            } else {
                int diff = nowYear - createYear;
                if (nowDay < createDay) {
                    diff--;
                }
                return diff == 1 ? context.getString(R.string.year_ago) : String.format(context.getString(R.string.years_ago), diff);
            }
        }
    }

    private class MyMenuClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Animation animation;
            subMenuImage.setRotation(0f); // zero out rotation angle since animation will start and end at proper angles

            if (listener.isActionBarOpen(getPosition())) {
                // close animation for menu 90 -> 0 degree
                animation = new RotateAnimation(90f, 0f, subMenuImage.getWidth() / 2, subMenuImage.getHeight() / 2);
            } else {
                // open animation for menu 0 -> degree
                animation = new RotateAnimation(0f, 90f, subMenuImage.getWidth() / 2, subMenuImage.getHeight() / 2);
            }

            animation.setDuration(200);
            animation.setFillAfter(true);
            subMenuImage.startAnimation(animation);
            listener.onActionMoreClicked(getPosition(), comment.getLeft() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics()));
        }
    }

    public interface Listener {
        public void onActionMoreClicked(int position, int left);
        public boolean isActionBarOpen(int position);
    }

}
