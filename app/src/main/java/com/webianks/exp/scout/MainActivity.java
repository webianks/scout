package com.webianks.exp.scout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<NamedEntities> {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ApiServices apiService = new RestClient().getApiService();
        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities("Mail from Ravi to me","json");

        //asynchronous call
        namedEntitiesCall.enqueue(this);
    }



    @Override
    public void onResponse(Call<NamedEntities> call, Response<NamedEntities> response) {

        if (response.isSuccessful()) {

            NamedEntities namedEntities = response.body();
            Toast.makeText(MainActivity.this,"Got the entities",Toast.LENGTH_LONG).show();

            Log.d(TAG,namedEntities.getEntities().get(0).getText());

        }
    }

    @Override
    public void onFailure(Call<NamedEntities> call, Throwable t) {

    }
}
