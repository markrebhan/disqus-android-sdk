package com.mrebhan.disqus.user;

import android.text.style.SubscriptSpan;
import android.util.Log;

import com.mrebhan.disqus.auth.AuthManager;
import com.mrebhan.disqus.datamodel.ResponseItem;
import com.mrebhan.disqus.datamodel.User;
import com.mrebhan.disqus.services.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Keep a reference of the current user in memory for now. This is just a wrapper to the user service
 * to quickly get a user that is currently logged in.
 */
@Singleton
public class CurrentUserManager {

    private static final int TIMEOUT_CACHE = 1000 * 60 * 60 * 24; // 1 day cache limit

    private UserService userService;
    private User user;
    private long lastNetworkCall;

    public CurrentUserManager(UserService userService) {
        this.userService = userService;
    }

    public void getCurrentUser(Callback<User> userCallback, boolean forceNetworkCall) {
        if (!forceNetworkCall && user != null && lastNetworkCall + TIMEOUT_CACHE < System.currentTimeMillis()) { // if user is in memory return in callback.
            userCallback.success(user, null);
        } else {
            userService.getCurrentUser(new MyGetCurrentUserWrapper(userCallback));
        }
    }

    private class MyGetCurrentUserWrapper implements Callback<ResponseItem<User>> {
        private Callback<User> callback;

        private MyGetCurrentUserWrapper(Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void success(ResponseItem<User> responseItem, Response response) {
            lastNetworkCall = System.currentTimeMillis();
            user = responseItem.getResponse();
            if (callback != null) {
                callback.success(user, response);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (callback != null) {
                callback.failure(error);
            }
            Log.e("", "Error fetching current user.", error);
        }
    }
}
