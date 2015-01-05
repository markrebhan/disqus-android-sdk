package com.mrebhan.disqus.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Post implements Parcelable {

    @SerializedName("isJuliaFlagged")
    boolean isJuliaFlagged;

    @SerializedName("isFlagged")
    boolean isFlagged;

    @SerializedName("forum")
    String forum;

    @SerializedName("parent")
    Long parentId;

    @SerializedName("author")
    Author author;

    @SerializedName("media")
    ArrayList<String> media;

    @SerializedName("isApproved")
    boolean isApproved;

    @SerializedName("dislikes")
    int dislikes;

    @SerializedName("raw_message")
    String rawMessage;

    @SerializedName("id")
    String postId;

    @SerializedName("thread")
    String threadId;

    @SerializedName("points")
    int points;

    @SerializedName("createdAt")
    Date createdDate;

    @SerializedName("isEdited")
    boolean isEdited;

    @SerializedName("message")
    String message;

    @SerializedName("isHighlighted")
    boolean isHighlighted;

    @SerializedName("ipAddress")
    String ipAddress;

    @SerializedName("isSpam")
    boolean isSpam;

    @SerializedName("isDeleted")
    boolean isDeleted;

    @SerializedName("likes")
    int likes;

    public Post() {
    }

    public Post(boolean isJuliaFlagged, boolean isFlagged, String forum, Long parentId, Author author, ArrayList<String> media, boolean isApproved, int dislikes, String rawMessage, String postId, String threadId, int points, Date createdDate, boolean isEdited, String message, boolean isHighlighted, String ipAddress, boolean isSpam, boolean isDeleted, int likes) {
        this.isJuliaFlagged = isJuliaFlagged;
        this.isFlagged = isFlagged;
        this.forum = forum;
        this.parentId = parentId;
        this.author = author;
        this.media = media;
        this.isApproved = isApproved;
        this.dislikes = dislikes;
        this.rawMessage = rawMessage;
        this.postId = postId;
        this.threadId = threadId;
        this.points = points;
        this.createdDate = createdDate;
        this.isEdited = isEdited;
        this.message = message;
        this.isHighlighted = isHighlighted;
        this.ipAddress = ipAddress;
        this.isSpam = isSpam;
        this.isDeleted = isDeleted;
        this.likes = likes;
    }

    public boolean isJuliaFlagged() {
        return isJuliaFlagged;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public String getForum() {
        return forum;
    }

    public long getParentId() {
        return parentId;
    }

    public Author getAuthor() {
        return author;
    }

    public ArrayList<String> getMedia() {
        return media;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public int getDislikes() {
        return dislikes;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getPostId() {
        return postId;
    }

    public String getThreadId() {
        return threadId;
    }

    public int getPoints() {
        return points;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public int getLikes() {
        return likes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isJuliaFlagged ? (byte) 1 : (byte) 0);
        dest.writeByte(isFlagged ? (byte) 1 : (byte) 0);
        dest.writeString(this.forum);
        dest.writeLong(this.parentId);
        dest.writeParcelable(this.author, 0);
        dest.writeSerializable(this.media);
        dest.writeByte(isApproved ? (byte) 1 : (byte) 0);
        dest.writeInt(this.dislikes);
        dest.writeString(this.rawMessage);
        dest.writeString(this.postId);
        dest.writeString(this.threadId);
        dest.writeInt(this.points);
        dest.writeLong(createdDate != null ? createdDate.getTime() : -1);
        dest.writeByte(isEdited ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeByte(isHighlighted ? (byte) 1 : (byte) 0);
        dest.writeString(this.ipAddress);
        dest.writeByte(isSpam ? (byte) 1 : (byte) 0);
        dest.writeByte(isDeleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.likes);
    }

    private Post(Parcel in) {
        this.isJuliaFlagged = in.readByte() != 0;
        this.isFlagged = in.readByte() != 0;
        this.forum = in.readString();
        this.parentId = in.readLong();
        this.author = in.readParcelable(Author.class.getClassLoader());
        this.media = (ArrayList<String>) in.readSerializable();
        this.isApproved = in.readByte() != 0;
        this.dislikes = in.readInt();
        this.rawMessage = in.readString();
        this.postId = in.readString();
        this.threadId = in.readString();
        this.points = in.readInt();
        long tmpCreatedDate = in.readLong();
        this.createdDate = tmpCreatedDate == -1 ? null : new Date(tmpCreatedDate);
        this.isEdited = in.readByte() != 0;
        this.message = in.readString();
        this.isHighlighted = in.readByte() != 0;
        this.ipAddress = in.readString();
        this.isSpam = in.readByte() != 0;
        this.isDeleted = in.readByte() != 0;
        this.likes = in.readInt();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
