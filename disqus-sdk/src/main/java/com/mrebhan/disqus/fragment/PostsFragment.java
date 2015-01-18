package com.mrebhan.disqus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;
import com.mrebhan.disqus.widgets.PaginatedAdapter;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostsFragment extends BaseFragment {

    public static final String ARG_THREAD_ID = ".PostsFragment.threadId";
    public static final String PREFIX_ADAPTER = ".PostsFragment.MyAdapter";

    @Inject
    ThreadPostsService threadPostsService;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_posts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            threadId = savedInstanceState.getString(ARG_THREAD_ID);
            adapter.onRestoreInstanceState(PREFIX_ADAPTER, savedInstanceState);
        } else {
            threadId = getArguments().getString(ARG_THREAD_ID);
            // get the first page of posts
            threadPostsService.getPosts(threadId, DisqusSdkProvider.publicKey, new MyGetPostsCallback());
        }

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

            if (currentPost.getParentId() == null) {

            }
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView profileImage;
            public TextView username;
            public TextView replyUsername;
            public TextView timeAgo;
            public TextView comment;
            public TextView upVotes;
            public ImageView subMenu;

            public MyViewHolder(View itemView) {
                super(itemView);
                profileImage = (ImageView) itemView.findViewById(R.id.img_avatar);
                username = (TextView) itemView.findViewById(R.id.txt_user_name);
                replyUsername = (TextView) itemView.findViewById(R.id.txt_reply_user);
                timeAgo = (TextView) itemView.findViewById(R.id.txt_time_ago);
                comment = (TextView) itemView.findViewById(R.id.txt_comment);
                upVotes = (TextView) itemView.findViewById(R.id.txt_up_votes);
                subMenu = (ImageView) itemView.findViewById(R.id.post_menu);
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
