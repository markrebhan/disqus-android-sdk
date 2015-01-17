package com.mrebhan.disqus;

import com.mrebhan.disqus.endpoints.threads.ListPosts;
import com.mrebhan.disqus.fragment.PostsFragment;
import com.mrebhan.disqus.json.GsonFactory;
import com.mrebhan.disqus.services.ThreadPostsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Client facing class to the Disqus SDK including configurations, authentication and DI setup
 */
public class DisqusSdkProvider {
    private static DisqusSdkProvider disqusSdkProvider;
    public static String publicKey;

    private ObjectGraph objectGraph;

    private DisqusSdkProvider(Builder builder) {
        this.objectGraph = ObjectGraph.create(new SdkModule());
        disqusSdkProvider = this;
        publicKey = builder.publicKey;
    }

    public static DisqusSdkProvider getInstance() {
        if (disqusSdkProvider == null) {
            throw new NullPointerException("Disqus SDK Provider must be initialized in your application. " +
                    "Add a builder to the onCreate() method on a class extending application");
        }

        return disqusSdkProvider;
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    @Module (
            injects = {
                    PostsFragment.class
            }
    )
    protected static class SdkModule {

        @Singleton
        @Provides
        RestAdapter providesRestAdapter() {
            return new RestAdapter
                    .Builder()
                    .setEndpoint("https://disqus.com/api")
                    .setConverter(new GsonConverter(GsonFactory.newGsonInstance()))
                    .build();
        }

        @Provides
        ThreadPostsService providesThreadPostService(RestAdapter restAdapter) {
            return restAdapter.create(ThreadPostsService.class);
        }
    }

    public static class Builder {

        private String publicKey;

        public Builder() {
        }

        public DisqusSdkProvider build() {
            Check.checkNotNull(publicKey, "A non null public key must be set!");

            return new DisqusSdkProvider(this);
        }

        public Builder setPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }
    }

}