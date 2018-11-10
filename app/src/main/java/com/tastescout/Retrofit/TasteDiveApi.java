package com.tastescout.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TasteDiveApi {

    @GET("similar")
    Call<Item> getSimilar(@Query("q")String query, @Query("k")String key,@Query("info")String info);
}
