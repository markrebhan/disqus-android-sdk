package com.mrebhan.disqus.services;

import com.mrebhan.disqus.datamodel.ResponseItem;
import com.mrebhan.disqus.datamodel.User;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Restful service endpoint to retrieve a user.
 */
public interface UserService {

    /**
     * Fetch the current user.
     */
    @GET("/3.0/users/details.json")
    public void getCurrentUser(Callback<ResponseItem<User>> callback);
}
