package com.tastescout.Retrofit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "items",primaryKeys = {"Name","Type","user"})
public class Result implements Parcelable
{

    @NonNull
    @SerializedName("Name")
    private String Name;

    @NonNull
    @SerializedName("Type")
    private String Type;

    @NonNull
    private String user="0";

    @SerializedName("wTeaser")
    private String wTeaser;

    @Ignore
    @SerializedName("wUrl")
    private String wUrl;

    @Ignore
    @SerializedName("yUrl")
    private String yUrl;

    @SerializedName("yID")
    private String yID;


    private Boolean liked=false;


    @Ignore
    public Result() {
        super();
    }


    public Result(@NonNull String Name, @NonNull String Type,@NonNull String user ,String wTeaser, String yID, Boolean liked){
        this.Name=Name;
        this.Type=Type;
        this.user=user;
        this.wTeaser=wTeaser;
        this.yID=yID;
        this.liked=liked;
    }

    protected Result(Parcel in) {
        Name = Objects.requireNonNull(in.readString());
        Type = Objects.requireNonNull(in.readString());
        user = Objects.requireNonNull(in.readString());
        wTeaser = in.readString();
        wUrl = in.readString();
        yUrl = in.readString();
        yID = in.readString();
        byte tmpLiked = in.readByte();
        liked = tmpLiked == 0 ? null : tmpLiked == 1;    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @NonNull
    public String getName() {
        return Name;
    }

    public void setName(@NonNull String name) {
        this.Name = name;
    }

    @NonNull
    public String getType() {
        return Type;
    }

    public void setType(@NonNull String type) {
        this.Type = type;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    public String getWTeaser() {
        return wTeaser;
    }

    public void setWTeaser(String wTeaser) {
        this.wTeaser = wTeaser;
    }

    public String getWUrl() {
        return wUrl;
    }

    public void setWUrl(String wUrl) {
        this.wUrl = wUrl;
    }

    public String getYUrl() {
        return yUrl;
    }

    public void setYUrl(String yUrl) {
        this.yUrl = yUrl;
    }

    public String getYID() {
        return yID;
    }

    public void setYID(String yID) {
        this.yID = yID;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Type);
        dest.writeString(user);
        dest.writeString(wTeaser);
        dest.writeString(wUrl);
        dest.writeString(yUrl);
        dest.writeString(yID);
        dest.writeByte((byte) (liked == null ? 0 : liked ? 1 : 2));

    }
}