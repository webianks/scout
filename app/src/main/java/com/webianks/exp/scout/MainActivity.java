package com.webianks.exp.scout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.webianks.exp.scout.model.NamedEntities;
import com.webianks.exp.scout.model.TypedRelations;
import com.webianks.exp.scout.network.ApiServices;
import com.webianks.exp.scout.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiServices apiService = new RestClient().getApiService();

        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities("PDFs from Ravi in the last 3 days","json");
        Call<TypedRelations> typedRelationsCall = apiService.getTypedRelations("PDFs from Ravi in the last 3 days","json");

        //asynchronous call
        namedEntitiesCall.enqueue(new Callback<NamedEntities>() {
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
        });

        typedRelationsCall.enqueue(new Callback<TypedRelations>() {
            @Override
            public void onResponse(Call<TypedRelations> call, Response<TypedRelations> response) {

                if (response.isSuccessful()) {

                    TypedRelations typedRelations = response.body();
                    Toast.makeText(MainActivity.this,"Got the relations",Toast.LENGTH_LONG).show();
                    Log.d(TAG,typedRelations.getTypedRelations().get(0).getType());

                }
            }

            @Override
            public void onFailure(Call<TypedRelations> call, Throwable t) {

            }
        });

    }
}
