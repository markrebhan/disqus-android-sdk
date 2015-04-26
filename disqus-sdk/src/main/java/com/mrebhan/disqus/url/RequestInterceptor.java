package com.mrebhan.disqus.url;

import com.mrebhan.disqus.DisqusSdkProvider;

public class RequestInterceptor implements retrofit.RequestInterceptor {
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_ACCESS_TOKEN = "access_token";

    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addQueryParam(PARAM_API_KEY, DisqusSdkProvider.publicKey);

        // add the authenticated user to the request if available
        if (accessToken != null) {
            request.addQueryParam(PARAM_ACCESS_TOKEN, accessToken);
        }
    }
}
