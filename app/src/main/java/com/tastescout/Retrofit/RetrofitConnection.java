package com.tastescout.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {
    private static RetrofitConnection mInstance;
    private static final String BASE_URL = "https://tastedive.com/api/";
    private Retrofit mRetrofit;

    private RetrofitConnection() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitConnection getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitConnection();
        }
        return mInstance;
    }

    public TasteDiveApi getJSONApi() {
        return mRetrofit.create(TasteDiveApi.class);
    }
}