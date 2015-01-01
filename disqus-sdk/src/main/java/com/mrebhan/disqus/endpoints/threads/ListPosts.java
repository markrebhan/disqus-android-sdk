package com.mrebhan.disqus.endpoints.threads;

import com.mrebhan.disqus.Check;
import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.endpoints.Include;
import com.mrebhan.disqus.endpoints.Order;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.endpoints.Related;

import java.util.Date;
import java.util.List;

/**
 * Returns a list of posts within a thread.
 */
public class ListPosts {

    public PaginatedList<Post> getListPosts(String threadId) {
        return getListPosts(threadId, null, null, null, null, null, null, null, null);
    }

    public PaginatedList<Post> getListPosts(String threadId, String forum, Date since, List<Related> related, Cursor cursor, Integer limit, String query, List<Include> include, Order order) {
        Check.checkNotNull(threadId, "A thread id must be specified in order to get posts!");

        return null;
    }
}
