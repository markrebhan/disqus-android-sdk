package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper of sorts for direct incoming responses of single entity requests
 */
public class ResponseItem<T extends Parcelable> implements Parcelable {

    @SerializedName("code")
    long code;

    @SerializedName("response")
    T response;

    public ResponseItem() {
    }

    public long getCode() {
        return code;
    }

    public T getResponse() {
        return response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.code);
        dest.writeParcelable(this.response, flags);
    }

    private ResponseItem(Parcel in) {
        this.code = in.readLong();
        this.response = in.readParcelable(Entity.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResponseItem> CREATOR = new Parcelable.Creator<ResponseItem>() {
        public ResponseItem createFromParcel(Parcel source) {
            return new ResponseItem(source);
        }

        public ResponseItem[] newArray(int size) {
            return new ResponseItem[size];
        }
    };
}
