package com.tastescout.Retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable
{

    @SerializedName("Similar")
    private Similar Similar;
    public final static Parcelable.Creator<Item> CREATOR = new Creator<Item>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    }
            ;

    private Item(Parcel in) {
        this.Similar = ((Similar) in.readValue((Similar.class.getClassLoader())));
    }

    public Item() {
    }

    public Similar getSimilar() {
        return Similar;
    }

    public void setSimilar(Similar similar) {
        this.Similar = similar;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Similar);
    }

    public int describeContents() {
        return 0;
    }

}