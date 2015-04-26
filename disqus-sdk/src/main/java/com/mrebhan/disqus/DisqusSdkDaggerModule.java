package com.mrebhan.disqus;

import android.content.Context;

import com.mrebhan.disqus.auth.AuthManager;
import com.mrebhan.disqus.auth.RefreshTokenBroadcastReceiver;
import com.mrebhan.disqus.fragment.ActionBarItem;
import com.mrebhan.disqus.fragment.CommentItem;
import com.mrebhan.disqus.fragment.LoginFragment;
import com.mrebhan.disqus.fragment.PostCommentItem;
import com.mrebhan.disqus.fragment.PostsAdapter;
import com.mrebhan.disqus.fragment.PostsFragment;
import com.mrebhan.disqus.json.GsonFactory;
import com.mrebhan.disqus.services.AccessTokenService;
import com.mrebhan.disqus.services.ThreadPostsService;
import com.mrebhan.disqus.services.UserService;
import com.mrebhan.disqus.url.RequestInterceptor;
import com.mrebhan.disqus.user.CurrentUserManager;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                LoginFragment.class,
                PostsFragment.class,
                PostsAdapter.class,
                CommentItem.class,
                ActionBarItem.class,
                PostCommentItem.class,

                /** Receivers **/
                RefreshTokenBroadcastReceiver.class,
        }
)
public class DisqusSdkDaggerModule {

    private Context appContext;

    public DisqusSdkDaggerModule(Context appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    RestAdapter providesRestAdapter(RequestInterceptor requestInterceptor) {
        return new RestAdapter
                .Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://disqus.com/api")
                .setConverter(new GsonConverter(GsonFactory.newGsonInstance()))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    @Provides
    @Singleton
    RequestInterceptor providesRequestInterceptor() {
        return new RequestInterceptor();
    }

    @Provides
    ThreadPostsService providesThreadPostService(RestAdapter restAdapter) {
        return restAdapter.create(ThreadPostsService.class);
    }

    @Provides
    AccessTokenService providesAccessTokenService(RestAdapter restAdapter) {
        return restAdapter.create(AccessTokenService.class);
    }

    @Provides
    UserService providesUserService(RestAdapter restAdapter) {
        return restAdapter.create(UserService.class);
    }

    @Singleton
    @Provides
    Picasso providesPicasso() {
        return new Picasso.Builder(appContext)
                .downloader(new OkHttpDownloader(appContext))
                .memoryCache(new LruCache(appContext))
                .build();
    }

    @Provides
    @Singleton
    Context providesContext() {
        return appContext;
    }

    @Provides
    @Singleton
    AuthManager providesAuthManager(AccessTokenService accessTokenService, RequestInterceptor requestInterceptor, CurrentUserManager currentUserManager) {
        return new AuthManager(appContext, accessTokenService, requestInterceptor, currentUserManager);
    }

    @Provides
    @Singleton
    CurrentUserManager providesCurrentUserManager(UserService userService) {
        return new CurrentUserManager(userService);
    }
}
