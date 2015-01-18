package com.mrebhan.disqus;

import com.mrebhan.disqus.fragment.PostsFragment;
import com.mrebhan.disqus.json.GsonFactory;
import com.mrebhan.disqus.services.ThreadPostsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
                PostsFragment.class,
                DisqusSdkProvider.class
        }
)
public class DisqusSdkDaggerModule {

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
}
