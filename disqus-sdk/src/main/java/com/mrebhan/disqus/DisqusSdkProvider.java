package com.mrebhan.disqus;

import com.mrebhan.disqus.endpoints.threads.ListPosts;
import com.mrebhan.disqus.fragment.PostsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

/**
 * Client facing class to the Disqus SDK including configurations, authentication and DI setup
 */
public class DisqusSdkProvider {
    private ObjectGraph objectGraph;
    private static DisqusSdkProvider disqusSdkProvider;

    private DisqusSdkProvider() {
        this.objectGraph = ObjectGraph.create(new SdkModule());
        disqusSdkProvider = this;
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
        ListPosts providesListPosts() {
            return new ListPosts();
        }
    }

    public static class Builder {

        // TODO add auth and configuration stuff here
        public Builder() {
        }

        public DisqusSdkProvider build() {
            return new DisqusSdkProvider();
        }
    }

}
