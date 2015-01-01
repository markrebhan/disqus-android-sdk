package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Author implements Parcelable {

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

    public Author() {
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

    private Author(Parcel in) {
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

    public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
}

