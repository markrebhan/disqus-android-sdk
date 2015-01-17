package com.mrebhan.disqus.endpoints.threads;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.endpoints.Include;
import com.mrebhan.disqus.endpoints.Order;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.endpoints.Related;
import com.mrebhan.disqus.services.ThreadPostsService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;

/**
 * Returns a list of posts within a thread.
 */
public class ListPosts {

    @Inject
    ThreadPostsService threadPostsService;

    public void getListPosts(String threadId, Callback<PaginatedList<Post>> callback) {
        getListPosts(threadId, null, null, null, null, null, null, null, null, callback);
    }

    public void getListPosts(String threadId, String forum, Date since, List<Related> related, Cursor cursor, Integer limit, String query, List<Include> include, Order order, Callback<PaginatedList<Post>> callback) {
        threadPostsService.getPosts(threadId, DisqusSdkProvider.publicKey, callback);
    }
}
