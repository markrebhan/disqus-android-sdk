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
import com.mrebhan.disqus.services.AccessTokenService;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Login fragment handles OAuth login process and redirect
 */
public class LoginFragment extends BaseFragment {
    @Inject
    AccessTokenService accessTokenService;

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
        webView.loadUrl( "https://disqus.com/api/oauth/2.0/authorize/?" +
                        "client_id=" + DisqusSdkProvider.publicKey + "&" +
                        "scope=read,write&" +
                        "response_type=code&" +
                        "redirect_uri=" + DisqusSdkProvider.redirectUri);

        return view;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // catch the callback url
            if (url.startsWith(DisqusSdkProvider.redirectUri)) {
                // parse the redirect url and login with the temp auth code;
                Log.d("", "Got Url " + url);
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter("code");
                if (code != null) {
                    accessTokenService.PostToken("authorization_code", DisqusSdkProvider.publicKey, DisqusSdkProvider.privateKey, DisqusSdkProvider.redirectUri, code, new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public interface Binder {
        void onUserAuthenticated(); // navigate back to the parent fragment once the user is authenticated
    }

    public interface BinderProvider {
        Binder createBinder(LoginFragment fragment);
    }
}
