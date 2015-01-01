package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Avatar implements Parcelable {

    @SerializedName("permalink")
    String permalinkUrl;

    @SerializedName("cache")
    String cacheUrl;

    public Avatar() {
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public String getCacheUrl() {
        return cacheUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.permalinkUrl);
        dest.writeString(this.cacheUrl);
    }

    private Avatar(Parcel in) {
        this.permalinkUrl = in.readString();
        this.cacheUrl = in.readString();
    }

    public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
        public Avatar createFromParcel(Parcel source) {
            return new Avatar(source);
        }

        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };
}
