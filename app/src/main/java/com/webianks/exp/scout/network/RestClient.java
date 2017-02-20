package com.webianks.exp.scout.network;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by R Ankit on 20-02-2017.
 */

public class RestClient {

    private ApiServices apiService;

    public RestClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.API_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        apiService = retrofit.create(ApiServices.class);

    }

    public ApiServices getApiService() {
        return apiService;
    }

}
