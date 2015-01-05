package com.mrebhan.disqus.endpoints.threads;

import com.mrebhan.disqus.Check;
import com.mrebhan.disqus.datamodel.Author;
import com.mrebhan.disqus.datamodel.Cursor;
import com.mrebhan.disqus.endpoints.Include;
import com.mrebhan.disqus.endpoints.Order;
import com.mrebhan.disqus.datamodel.PaginatedList;
import com.mrebhan.disqus.datamodel.Post;
import com.mrebhan.disqus.endpoints.Related;

import java.util.ArrayList;
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

        // some temp list data
        ArrayList<Post> posts = new ArrayList<>();

        Author author0 = new Author("ballsack", null, "boo", "", false, false, null, null, "1", false, "@");
        Post post0 = new Post(false, false, "12", null, author0, null, true, 0, "this is a message to you sexy", "12", "1", 1, new Date(1420152879000l), false, "this is a message to you sexy", false, null, false, false, 1);
        posts.add(post0);

        Author author1 = new Author("woooooahh", null, "boo", "", false, false, null, null, "1", false, "@");
        Post post1 = new Post(false, false, "12", null, author1, null, true, 0, "lets do someting fool", "12", "1", 4, new Date(1420152879000l), false, "lets do someting fool", false, null, false, false, 1);
        posts.add(post1);

        Author author2 = new Author("markyMark", null, "boo", "", false, false, null, null, "1", false, "@");
        Post post2 = new Post(false, false, "12", null, author2, null, true, 0, "OMG THE LOLZ HAXORS ARE EVERYWHERE WTF IS THIS SHIT", "12", "1", 2, new Date(1420152879000l), false, "OMG THE LOLZ HAXORS ARE EVERYWHERE WTF IS THIS SHIT", false, null, false, false, 1);
        posts.add(post2);

        cursor = new Cursor(false, null, false, null, null, null, false);

        return new PaginatedList<>(cursor, 0, posts);
    }
}
