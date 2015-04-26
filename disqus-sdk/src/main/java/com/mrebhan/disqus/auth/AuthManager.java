package com.mrebhan.disqus.auth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.datamodel.AccessToken;
import com.mrebhan.disqus.services.AccessTokenService;
import com.mrebhan.disqus.url.RequestInterceptor;
import com.mrebhan.disqus.user.CurrentUserManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The auth manager handles getting and storing authentication information.
 */
@Singleton
public class AuthManager {

    private static final String PREF_AUTH = "AuthManager.Preferences";
    private static final String PREF_TOKEN = ".access_token";
    private static final String PREF_EXPIRE = ".expires_in";
    private static final String PREF_REFRESH_TOKEN = ".refresh_token";
    private static final String PREF_SCOPE = ".scope";

    private Context appContext;
    private AccessTokenService accessTokenService;
    private RequestInterceptor requestInterceptor;
    private CurrentUserManager currentUserManager;

    private List<AuthenticationListener> listeners = new ArrayList<>();

    @Inject
    public AuthManager(Context appContext, AccessTokenService accessTokenService, RequestInterceptor requestInterceptor, CurrentUserManager currentUserManager) {
        this.appContext = appContext;
        this.accessTokenService = accessTokenService;
        this.requestInterceptor = requestInterceptor;
        this.currentUserManager = currentUserManager;
        checkRefresh();
        setRequestInterceptor();
    }

    public void addListener(AuthenticationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AuthenticationListener listener) {
        listeners.remove(listener);
    }

    public boolean isAuthenticated() {
        return getSharedPreferences().getString(PREF_TOKEN, null) != null;
    }

    public String getAccessToken() {
        return getSharedPreferences().getString(PREF_TOKEN, null);
    }

    public void logout() {
        clearPrefs();

        for (AuthenticationListener listener : listeners) {
            listener.onLogout();
        }
    }

    public void authorizeAsync(String code) {
        if (code != null) {
            accessTokenService.PostToken("authorization_code", DisqusSdkProvider.publicKey, DisqusSdkProvider.privateKey, DisqusSdkProvider.redirectUri, code, new MyGetAccessTokensCallback(true));
        } else {
            throw new NullPointerException("Code must be a non-null string!");
        }
    }

    public void postRefreshTokenAsync() {
        if (getRefreshToken() != null) {
            accessTokenService.PostRefreshToken("refresh_token", DisqusSdkProvider.publicKey, DisqusSdkProvider.privateKey, getRefreshToken(), new MyGetAccessTokensCallback(false));
        }
    }

    private String getRefreshToken() {
        return getSharedPreferences().getString(PREF_REFRESH_TOKEN, null);
    }

    private long getExpireTime() {
        return getSharedPreferences().getLong(PREF_EXPIRE, 0);
    }

    private SharedPreferences getSharedPreferences() {
        return appContext.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE);
    }

    private void writeToSharedPrefs(AccessToken accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_TOKEN, accessToken.getAccessToken());
        editor.putLong(PREF_EXPIRE, (accessToken.getExpiresIn() * 1000l) + System.currentTimeMillis() - (60 * 1000));
        editor.putString(PREF_REFRESH_TOKEN, accessToken.getRefreshToken());
        editor.putString(PREF_SCOPE, accessToken.getScope());
        editor.apply();
    }

    private void clearPrefs() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Setup an alarm to refresh tokens when they expire. This will only fire off when the process
     * is running. We must check tokens on startup
     */
    private void setupAlarm() {
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DisqusSdkProvider.ACTION_REFRESH_TOKEN);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, -1, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getExpireTime(), pendingIntent);
    }

    private void setRequestInterceptor() {
        requestInterceptor.setAccessToken(getAccessToken());
    }

    private void checkRefresh() {
        if (System.currentTimeMillis() > getExpireTime()) {
            // need to get refresh tokens as they have expired.
            postRefreshTokenAsync();
        } else {
            setupAlarm();
        }
    }

    private class MyGetAccessTokensCallback implements Callback<AccessToken> {
        private boolean isLogin;

        private MyGetAccessTokensCallback(boolean isLogin) {
            this.isLogin = isLogin;
        }

        @Override
        public void success(AccessToken accessToken, Response response) {
            if (response.getStatus() == 200) {
                writeToSharedPrefs(accessToken); // save access token data to shared prefs.
                setupAlarm(); // setup refresh token alarm.
                setRequestInterceptor(); // set value of access token on request intercept

                if (isLogin) {
                    for (AuthenticationListener listener : listeners) {
                        listener.onLogin(); // notify listeners about login.
                        currentUserManager.getCurrentUser(null, true); // force a network call to get current user and add to memory.
                    }
                }
            } else {
                if (isLogin) {
                    for (AuthenticationListener listener : listeners) {
                        listener.onLoginFailed("Http response was " + response.getStatus());
                    }
                } else { // token refresh failed. logout.
                    logout();
                    for (AuthenticationListener listener : listeners) {
                        listener.onLogout();
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (isLogin) {
                for (AuthenticationListener listener : listeners) {
                    listener.onLoginFailed(error.getKind().name());
                }
            } else { // token refresh failed. logout.
                logout();
                for (AuthenticationListener listener : listeners) {
                    listener.onLogout();
                }
            }
        }
    }

    // TODO add proper error messaging
    public interface AuthenticationListener {
        void onLogin();
        void onLoginFailed(String error);
        void onLogout();
    }
}
