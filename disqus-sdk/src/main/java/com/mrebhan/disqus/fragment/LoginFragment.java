package com.mrebhan.disqus.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.auth.AuthManager;
import com.mrebhan.disqus.services.AccessTokenService;
import com.mrebhan.disqus.url.UrlParam;

import javax.inject.Inject;

/**
 * Login fragment handles OAuth login process and redirect
 */
public class LoginFragment extends BaseFragment implements AuthManager.AuthenticationListener {
    private static final String AUTHORIZE_URL = "https://disqus.com/api/oauth/2.0/authorize/";

    @Inject
    AuthManager authManager;

    private Binder binder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        binder = ((BinderProvider) getParentFragment()).createBinder(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        WebView webView = (WebView) view.findViewById(R.id.authWebView);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url = new UrlParam.Builder(AUTHORIZE_URL).
                addParam("client_id", DisqusSdkProvider.publicKey).
                addParam("scope", "read,write").
                addParam("response_type", "code").
                addParam("redirect_uri", DisqusSdkProvider.redirectUri).
                build().
                getEncodedUrl();

        webView.loadUrl(url);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        authManager.addListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        authManager.removeListener(this);
    }

    @Override
    public void onLogin() {
        binder.onUserAuthenticated(true);
    }

    @Override
    public void onLoginFailed(String error) {
        binder.onUserAuthenticated(false);
    }

    @Override
    public void onLogout() {
        /** Not used**/
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // catch the callback url
            if (url.contains(DisqusSdkProvider.redirectUri)) {
                // parse the redirect url and login with the temp auth code;
                Log.d("", "Got Url " + url);
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter("code");
                authManager.authorizeAsync(code);
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public interface Binder {
        void onUserAuthenticated(boolean success); // navigate back to the parent fragment once the user is authenticated
    }

    public interface BinderProvider {
        Binder createBinder(LoginFragment fragment);
    }
}
