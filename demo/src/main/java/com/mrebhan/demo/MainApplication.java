package com.mrebhan.demo;

import android.app.Application;

import com.mrebhan.disqus.DisqusSdkProvider;

public class MainApplication extends Application {
    private DisqusSdkProvider disqusSdkProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        disqusSdkProvider = new DisqusSdkProvider.Builder().build();
    }
}
