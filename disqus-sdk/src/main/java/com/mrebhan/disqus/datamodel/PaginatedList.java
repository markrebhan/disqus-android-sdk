package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaginatedList<T extends Parcelable> implements Parcelable {

    @SerializedName("cursor")
    Cursor cursor;

    @SerializedName("code")
    long code;

    @SerializedName("response")
    ArrayList<T> responseData;

    public Cursor getCursor() {
        return cursor;
    }

    public long getCode() {
        return code;
    }

    public ArrayList<T> getResponseData() {
        return new ArrayList<>(responseData); // return a copy not own so data is preserved
    }

    public PaginatedList() {
    }

    public PaginatedList(Cursor cursor, long code, ArrayList<T> responseData) {
        this.cursor = cursor;
        this.code = code;
        this.responseData = responseData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cursor, 0);
        dest.writeLong(this.code);
        dest.writeList(this.responseData);
    }

    private PaginatedList(Parcel in) {
        this.cursor = in.readParcelable(Cursor.class.getClassLoader());
        this.code = in.readLong();
        in.readList(this.responseData, Entity.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaginatedList> CREATOR = new Parcelable.Creator<PaginatedList>() {
        public PaginatedList createFromParcel(Parcel source) {
            return new PaginatedList(source);
        }

        public PaginatedList[] newArray(int size) {
            return new PaginatedList[size];
        }
    };
}
