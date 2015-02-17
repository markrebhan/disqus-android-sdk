package com.mrebhan.demo;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import com.mrebhan.disqus.DisqusSdkProvider;

public class MainApplication extends Application {
    private DisqusSdkProvider disqusSdkProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        disqusSdkProvider = new DisqusSdkProvider.Builder()
                .setPublicKey(BuildConfig.PUBLIC_KEY)
                .setPrivateKey(BuildConfig.PRIVATE_KEY)
                .setRedirectUri(BuildConfig.REDIRECT_URI)
                .setContext(getApplicationContext())
                .build();
    }
}
