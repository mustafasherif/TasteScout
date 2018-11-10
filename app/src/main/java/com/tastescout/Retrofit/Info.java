package com.tastescout.Retrofit;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Info implements Parcelable
{

    @SerializedName("Name")
    private String Name;

    @SerializedName("Type")
    private String Type;

    @SerializedName("wTeaser")
    private String wTeaser;

    @SerializedName("wUrl")
    private String wUrl;

    @SerializedName("yUrl")
    private String yUrl;

    @SerializedName("yID")
    private String yID;

    public final static Parcelable.Creator<Info> CREATOR = new Creator<Info>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        public Info[] newArray(int size) {
            return (new Info[size]);
        }

    }
            ;

    protected Info(Parcel in) {
        this.Name = ((String) in.readValue((String.class.getClassLoader())));
        this.Type = ((String) in.readValue((String.class.getClassLoader())));
        this.wTeaser = ((String) in.readValue((String.class.getClassLoader())));
        this.wUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.yUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.yID = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Info() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Name);
        dest.writeValue(Type);
        dest.writeValue(wTeaser);
        dest.writeValue(wUrl);
        dest.writeValue(yUrl);
        dest.writeValue(yID);
    }

    public int describeContents() {
        return 0;
    }

}