package com.mrebhan.disqus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;

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
    private PostsAdapter adapter;
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
        adapter = new PostsAdapter();

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
