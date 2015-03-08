package com.mrebhan.disqus.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mrebhan.disqus.DisqusSdkDaggerModule;
import com.mrebhan.disqus.DisqusSdkProvider;

import javax.inject.Inject;

public class RefreshTokenBroadcastReceiver extends BroadcastReceiver {

    @Inject
    AuthManager authManager;

    public RefreshTokenBroadcastReceiver() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        authManager.postRefreshTokenAsync();
    }
}
