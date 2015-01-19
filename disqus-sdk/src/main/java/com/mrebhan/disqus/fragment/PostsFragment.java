package com.mrebhan.disqus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.datamodel.Avatar;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;
import com.mrebhan.disqus.widgets.PaginatedAdapter;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostsFragment extends BaseFragment {

    public static final String ARG_THREAD_ID = ".PostsFragment.threadId";
    public static final String PREFIX_ADAPTER = ".PostsFragment.MyAdapter";

    @Inject
    ThreadPostsService threadPostsService;
    @Inject
    Picasso picasso;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter adapter;
    private String threadId;

    public static PostsFragment getInstance(String threadId) {
        PostsFragment postsFragment = new PostsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ARG_THREAD_ID, threadId);
        postsFragment.setArguments(bundle);
        return postsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyAdapter();

        if (savedInstanceState != null) {
            threadId = savedInstanceState.getString(ARG_THREAD_ID);
            adapter.onRestoreInstanceState(PREFIX_ADAPTER, savedInstanceState);
        } else {
            threadId = getArguments().getString(ARG_THREAD_ID);
            // get the first page of posts
            threadPostsService.getPosts(threadId, DisqusSdkProvider.publicKey, new MyGetPostsCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_posts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_THREAD_ID, threadId);
        adapter.onSaveInstanceState(PREFIX_ADAPTER, outState);
    }

    private class MyAdapter extends PaginatedAdapter<Post, MyAdapter.MyViewHolder> {

        @Override
        protected void loadNextPage(String cursorId, Callback<PaginatedList<Post>> callback) {
            threadPostsService.getNextPage(cursorId, DisqusSdkProvider.publicKey, callback);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            Post currentPost = getItem(position);
            holder.username.setText(currentPost.getAuthor().getUsername());
            holder.comment.setText(currentPost.getRawMessage());
            holder.upVotes.setText(Integer.toString(currentPost.getLikes()));
            holder.timeAgo.setText(buildTimeAgo(currentPost.getCreatedDate()));

            Avatar avatar = currentPost.getAuthor().getAvatar();
            if (avatar.getPermalinkUrl() != null) {
                picasso.load(avatar.getPermalinkUrl()).error(R.drawable.no_avatar_92).into(holder.profileImage);
            } else {
                picasso.load(R.drawable.no_avatar_92).into(holder.profileImage);
            }

            holder.subMenu.setOnClickListener(new MyMenuClickListener(holder));
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
                return getString(R.string.moments_ago);
            } else if (nowYear == createYear && nowDay == createDay && nowHour == createHour) {
                int diff = nowMinute - createMinute;
                return diff == 1 ? getString(R.string.minute_ago) : String.format(getString(R.string.minutes_ago), diff);
            } else if (nowYear == createYear && nowDay == createDay) {
                if (nowHour - 1 == createHour && nowMinute < createMinute) {
                    int diff = nowMinute + (60 - createMinute);
                    return diff == 1 ? getString(R.string.minute_ago) : String.format(getString(R.string.minutes_ago), diff);
                } else {
                    int diff = nowHour - createHour;
                    return diff == 1 ? getString(R.string.hour_ago) : String.format(getString(R.string.hours_ago), diff);
                }
            } else if (nowYear == createYear) {
                if (nowDay - 1 == createDay && nowHour < createHour) {
                    int diff = nowHour + (24 - createHour);
                    return diff == 1 ? getString(R.string.hour_ago) : String.format(getString(R.string.hours_ago), diff);
                } else {
                    int diff = nowDay - createDay;
                    return diff == 1 ? getString(R.string.day_ago) : String.format(getString(R.string.days_ago), diff);
                }
            } else {
                if (nowYear - 1 == createYear && nowDay < createDay) {
                    int diff = nowDay + (365 - createDay);
                    return diff == 1 ? getString(R.string.day_ago) : String.format(getString(R.string.days_ago), diff);
                } else {
                    int diff = nowYear - createYear;
                    if (nowDay < createDay) {
                        diff--;
                    }
                    return diff == 1 ? getString(R.string.year_ago) : String.format(getString(R.string.years_ago), diff);
                }
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView profileImage;
            public TextView username;
            public TextView replyUsername;
            public TextView timeAgo;
            public TextView comment;
            public TextView upVotes;
            public View upVoteContainer;
            public View downVoteContainer;
            public View subMenu;
            public FrameLayout actions;
            public TextView moreShare;
            public TextView moreReply;
            public ImageView moreMinimize;
            public ImageView moreFlag;

            public MyViewHolder(View itemView) {
                super(itemView);
                profileImage = (ImageView) itemView.findViewById(R.id.img_avatar);
                username = (TextView) itemView.findViewById(R.id.txt_user_name);
                replyUsername = (TextView) itemView.findViewById(R.id.txt_reply_user);
                timeAgo = (TextView) itemView.findViewById(R.id.txt_time_ago);
                comment = (TextView) itemView.findViewById(R.id.txt_comment);
                upVotes = (TextView) itemView.findViewById(R.id.txt_up_votes);
                upVoteContainer = itemView.findViewById(R.id.frame_up_vote);
                downVoteContainer = itemView.findViewById(R.id.frame_down_vote);
                subMenu = itemView.findViewById(R.id.post_menu);
                actions = (FrameLayout) itemView.findViewById(R.id.frame_actions);
                moreShare = (TextView) itemView.findViewById(R.id.more_share);
                moreReply = (TextView) itemView.findViewById(R.id.more_reply);
                moreMinimize = (ImageView) itemView.findViewById(R.id.more_minimize);
                moreFlag = (ImageView) itemView.findViewById(R.id.more_flag);
            }
        }

        private class MyMenuClickListener implements View.OnClickListener {
            MyViewHolder holder;

            private MyMenuClickListener(MyViewHolder holder) {
                this.holder = holder;
            }


            @Override
            public void onClick(View v) {

                if (holder.actions.getVisibility() == View.GONE) {
                    holder.actions.setVisibility(View.VISIBLE);
                    Animation animation = new RotateAnimation(0f, 90f, holder.subMenu.getWidth() / 2, holder.subMenu.getHeight() / 2);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    holder.subMenu.startAnimation(animation);
                } else {
                    holder.actions.setVisibility(View.GONE);
                    Animation animation = new RotateAnimation(90f, 0f, holder.subMenu.getWidth() / 2, holder.subMenu.getHeight() / 2);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    holder.subMenu.startAnimation(animation);
                }
            }
        }
    }

    private class MyGetPostsCallback implements Callback<PaginatedList<Post>> {
        @Override
        public void success(PaginatedList<Post> posts, Response response) {
            adapter.addList(posts);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("PostsFragment", "error getting list post", error);
        }
    }
}
