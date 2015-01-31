package com.mrebhan.disqus;

import android.content.Context;

import com.mrebhan.disqus.fragment.CommentItem;
import com.mrebhan.disqus.fragment.PostsAdapter;
import com.mrebhan.disqus.fragment.PostsFragment;
import com.mrebhan.disqus.json.GsonFactory;
import com.mrebhan.disqus.services.ThreadPostsService;
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
                PostsFragment.class,
                PostsAdapter.class,
                CommentItem.class
        }
)
public class DisqusSdkDaggerModule {

    private Context appContext;

    public DisqusSdkDaggerModule(Context appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    RestAdapter providesRestAdapter() {
        return new RestAdapter
                .Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint("https://disqus.com/api")
                .setConverter(new GsonConverter(GsonFactory.newGsonInstance()))
                .build();
    }

    @Provides
    ThreadPostsService providesThreadPostService(RestAdapter restAdapter) {
        return restAdapter.create(ThreadPostsService.class);
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
}
