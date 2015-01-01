package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cursor implements Parcelable {

    @SerializedName("hasPrev")
    boolean hasPrev;

    @SerializedName("prev")
    String previousPage;

    @SerializedName("hasNext")
    boolean hasNext;

    @SerializedName("next")
    String nextPage;

    @SerializedName("total")
    String total;

    @SerializedName("id")
    String id;

    @SerializedName("more")
    boolean more;

    public Cursor() {
    }

    public boolean hasPrev() {
        return hasPrev;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getTotal() {
        return total;
    }

    public String getId() {
        return id;
    }

    public boolean hasMore() {
        return more;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(hasPrev ? (byte) 1 : (byte) 0);
        dest.writeString(this.previousPage);
        dest.writeByte(hasNext ? (byte) 1 : (byte) 0);
        dest.writeString(this.nextPage);
        dest.writeString(this.total);
        dest.writeString(this.id);
        dest.writeByte(more ? (byte) 1 : (byte) 0);
    }

    private Cursor(Parcel in) {
        this.hasPrev = in.readByte() != 0;
        this.previousPage = in.readString();
        this.hasNext = in.readByte() != 0;
        this.nextPage = in.readString();
        this.total = in.readString();
        this.id = in.readString();
        this.more = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Cursor> CREATOR = new Parcelable.Creator<Cursor>() {
        public Cursor createFromParcel(Parcel source) {
            return new Cursor(source);
        }

        public Cursor[] newArray(int size) {
            return new Cursor[size];
        }
    };
}
