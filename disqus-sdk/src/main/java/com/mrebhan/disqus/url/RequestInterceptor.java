package com.mrebhan.disqus.url;

import com.mrebhan.disqus.DisqusSdkProvider;

public class RequestInterceptor implements retrofit.RequestInterceptor {
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_ACCESS_TOKEN = "access_token";

    private String accessToken;
    private boolean removeApiKey;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setIncludeApiKey(boolean removeApiKey) {
        this.removeApiKey = removeApiKey;
    }

    @Override
    public void intercept(RequestFacade request) {
        // add an api key to every request
        if (!removeApiKey) {
            request.addQueryParam(PARAM_API_KEY, DisqusSdkProvider.publicKey);
        }

        // add the authenticated user to the request if available
        if (accessToken != null) {
            request.addQueryParam(PARAM_ACCESS_TOKEN, accessToken);
        }
    }
}
