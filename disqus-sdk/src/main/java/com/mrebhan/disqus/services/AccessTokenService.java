package com.mrebhan.disqus.services;

import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.Callback;

/**
 * Restful endpoint used to retrieve a users access token once the auth code has been retrieved.
 */
public interface AccessTokenService {

    @POST("/oauth/2.0/access_token/")
    void PostToken(@Query("grant_type") String grantType,
                   @Query("client_id") String clientId,
                   @Query("client_secret") String clientSecret,
                   @Query("redirect_uri") String redirectUrl,
                   @Query("code") String authCode,
                   Callback<String> callback);
}
