package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("username")
    String username;

    @SerializedName("about")
    String about;

    @SerializedName("name")
    String name;

    @SerializedName("url")
    String url;

    @SerializedName("isFollowing")
    boolean isFollowing;

    @SuppressWarnings("isFollowedBy")
    boolean isFollowedBy;

    @SuppressWarnings("profileUrl")
    String profileUrl;

    @SerializedName("avatar")
    Avatar avatar;

    @SerializedName("id")
    String id;

    @SerializedName("isAnonymous")
    boolean isAnonymous;

    @SerializedName("email")
    String email;

    public User() {
    }

    public User(String username, String about, String name, String url, boolean isFollowing, boolean isFollowedBy, String profileUrl, Avatar avatar, String id, boolean isAnonymous, String email) {
        this.username = username;
        this.about = about;
        this.name = name;
        this.url = url;
        this.isFollowing = isFollowing;
        this.isFollowedBy = isFollowedBy;
        this.profileUrl = profileUrl;
        this.avatar = avatar;
        this.id = id;
        this.isAnonymous = isAnonymous;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public boolean isFollowedBy() {
        return isFollowedBy;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.about);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeByte(isFollowing ? (byte) 1 : (byte) 0);
        dest.writeByte(isFollowedBy ? (byte) 1 : (byte) 0);
        dest.writeString(this.profileUrl);
        dest.writeParcelable(this.avatar, 0);
        dest.writeString(this.id);
        dest.writeByte(isAnonymous ? (byte) 1 : (byte) 0);
        dest.writeString(this.email);
    }

    private User(Parcel in) {
        this.username = in.readString();
        this.about = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.isFollowing = in.readByte() != 0;
        this.isFollowedBy = in.readByte() != 0;
        this.profileUrl = in.readString();
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
        this.id = in.readString();
        this.isAnonymous = in.readByte() != 0;
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

