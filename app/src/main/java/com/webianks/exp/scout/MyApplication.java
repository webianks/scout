package com.webianks.exp.scout;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by R Ankit on 21-02-2017.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .clientKey("myClientKey")
                .server("https://webianks.herokuapp.com/parse/")
                .build());

    }
}
