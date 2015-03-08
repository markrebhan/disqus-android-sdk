package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AccessToken implements Parcelable {

    @SerializedName("username")
    String username;

    @SerializedName("user_id")
    Long userId;

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("expires_in")
    Long expiresIn;

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("state")
    String state;

    @SerializedName("scope")
    String scope;

    @SerializedName("refresh_token")
    String refreshToken;

    public AccessToken() {
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getState() {
        return state;
    }

    public String getScope() {
        return scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeValue(this.userId);
        dest.writeString(this.accessToken);
        dest.writeValue(this.expiresIn);
        dest.writeString(this.tokenType);
        dest.writeString(this.state);
        dest.writeString(this.scope);
        dest.writeString(this.refreshToken);
    }

    private AccessToken(Parcel in) {
        this.username = in.readString();
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.accessToken = in.readString();
        this.expiresIn = (Long) in.readValue(Long.class.getClassLoader());
        this.tokenType = in.readString();
        this.state = in.readString();
        this.scope = in.readString();
        this.refreshToken = in.readString();
    }

    public static final Parcelable.Creator<AccessToken> CREATOR = new Parcelable.Creator<AccessToken>() {
        public AccessToken createFromParcel(Parcel source) {
            return new AccessToken(source);
        }

        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };
}
