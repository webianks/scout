package com.webianks.exp.scout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by R Ankit on 20-02-2017.
 */

interface ApiServices {

    @GET("/calls/text/TextGetRankedNamedEntities?apikey="+BuildConfig.API_KEY+"&model="+BuildConfig.MODEL)
    Call<NamedEntities> getNamedEntities(@Query("text") String text, @Query("outputMode") String outputMode);

}
