package com.tastescout.Retrofit;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Similar implements Parcelable
{

    @SerializedName("Info")
    private List<Info> Info ;
    @SerializedName("Results")
    private List<Result> Results;


    private Similar(Parcel in) {
        Info = in.createTypedArrayList(com.tastescout.Retrofit.Info.CREATOR);
        Results = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Creator<Similar> CREATOR = new Creator<Similar>() {
        @Override
        public Similar createFromParcel(Parcel in) {
            return new Similar(in);
        }

        @Override
        public Similar[] newArray(int size) {
            return new Similar[size];
        }
    };

    public List<Info> getInfo() {
        return Info;
    }

    public void setInfo(List<Info> info) {
        this.Info = info;
    }

    public List<Result> getResults() {
        return Results;
    }

    public void setResults(List<Result> results) {
        this.Results = results;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Info);
        dest.writeTypedList(Results);
    }
}