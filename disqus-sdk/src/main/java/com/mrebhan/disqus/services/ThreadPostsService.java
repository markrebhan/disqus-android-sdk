package com.mrebhan.disqus.services;

import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface ThreadPostsService {

    @GET("/3.0/threads/listPosts.json")
    void getPosts(@Query("thread") String threadId, @Query("api_key") String apiKey, Callback<PaginatedList<Post>> posts);

    @GET("/3.0/threads/listPosts.json")
    void getNextPage(@Query("cursor") String cursorId, @Query("api_key") String apiKey, Callback<PaginatedList<Post>> posts);

}
