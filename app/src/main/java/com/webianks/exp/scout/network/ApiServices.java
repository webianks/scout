package com.webianks.exp.scout.network;

import com.webianks.exp.scout.BuildConfig;
import com.webianks.exp.scout.model.NamedEntities;
import com.webianks.exp.scout.model.TypedRelations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by R Ankit on 20-02-2017.
 */

public interface ApiServices {

    @GET("/calls/text/TextGetRankedNamedEntities?apikey="+ BuildConfig.API_KEY)
    Call<NamedEntities> getNamedEntities(@Query("text") String text, @Query("outputMode") String outputMode,@Query("model") String model);

    @GET("/calls/text/TextGetTypedRelations?apikey="+ BuildConfig.API_KEY)
    Call<TypedRelations> getTypedRelations(@Query("text") String text, @Query("outputMode") String outputMode,@Query("model") String model);

}
