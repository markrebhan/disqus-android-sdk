package com.mrebhan.disqus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.endpoints.threads.ListPosts;

import java.util.ArrayList;

import javax.inject.Inject;

public class PostsFragment extends BaseFragment {

    @Inject
    ListPosts listPosts;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, null, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_posts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private ArrayList<Post> allPosts = new ArrayList<>();

        private MyAdapter() {
            //TODO wire up to real shit and add cursor logic
            addPage(listPosts.getListPosts("N/A"));
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Post currentPost = allPosts.get(position);
            holder.username.setText(currentPost.getAuthor().getUsername());
            holder.comment.setText(currentPost.getMessage());
            holder.upVotes.setText(Integer.toString(currentPost.getLikes()));

            if (currentPost.getParentId() == null) {

            }
        }

        @Override
        public int getItemCount() {
            return allPosts.size();
        }

        public void addPage(PaginatedList<Post> postPaginatedList) {
            allPosts.addAll(postPaginatedList.getResponseData());
            notifyDataSetChanged();
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
}
