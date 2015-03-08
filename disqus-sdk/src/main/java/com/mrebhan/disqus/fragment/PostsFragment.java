package com.mrebhan.disqus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.auth.AuthManager;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.services.ThreadPostsService;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostsFragment extends BaseFragment implements LoginFragment.BinderProvider{

    public static final String ARG_THREAD_ID = ".PostsFragment.threadId";
    public static final String PREFIX_ADAPTER = ".PostsFragment.MyAdapter";

    @Inject
    ThreadPostsService threadPostsService;
    @Inject
    AuthManager authManager;

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;

    private RecyclerView.LayoutManager layoutManager;
    private PostsAdapter adapter;
    private String threadId;

    private MyBinder binder = new MyBinder();
    private Fragment loginFragment;

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
            threadPostsService.getPosts(threadId, new MyGetPostsCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_posts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addButton = (FloatingActionButton) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new MyOnAddClickListener());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_THREAD_ID, threadId);
        adapter.onSaveInstanceState(PREFIX_ADAPTER, outState);
    }

    @Override
    public LoginFragment.Binder createBinder(LoginFragment fragment) {
        return binder;
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

    private class MyOnAddClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // if authenticated add comment row or show auth page
            if (authManager.isAuthenticated()) {
                adapter.addPostCommentRow();
            } else {
                loginFragment = new LoginFragment();
                getChildFragmentManager().beginTransaction().add(R.id.loginChildContainer, loginFragment).addToBackStack(null).commit();
            }
        }
    }

    private class MyBinder implements LoginFragment.Binder {

        @Override
        public void onUserAuthenticated(boolean success) {
            getChildFragmentManager().beginTransaction().remove(loginFragment).commit();
            loginFragment = null;

            if (!success) {
                Toast.makeText(getActivity(), "Error logging in. Please try again later.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
