package com.webianks.exp.scout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.webianks.exp.scout.model.NamedEntities;
import com.webianks.exp.scout.model.TypedRelations;
import com.webianks.exp.scout.network.ApiServices;
import com.webianks.exp.scout.network.RestClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testCalls();

    }

    private void testCalls() {

        ApiServices apiService = new RestClient().getApiService();

        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities("PDFs from Ravi in the last 3 days", "json");
        Call<TypedRelations> typedRelationsCall = apiService.getTypedRelations("PDFs from Ravi in the last 3 days", "json");

        //asynchronous call
        namedEntitiesCall.enqueue(new Callback<NamedEntities>() {
            @Override
            public void onResponse(Call<NamedEntities> call, Response<NamedEntities> response) {

                if (response.isSuccessful()) {
                    NamedEntities namedEntities = response.body();
                    Toast.makeText(MainActivity.this, "Got the entities", Toast.LENGTH_LONG).show();
                    Log.d(TAG, namedEntities.getEntities().get(0).getText());
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
                    Toast.makeText(MainActivity.this, "Got the relations", Toast.LENGTH_LONG).show();
                    Log.d(TAG, typedRelations.getTypedRelations().get(0).getType());

                }
            }

            @Override
            public void onFailure(Call<TypedRelations> call, Throwable t) {

            }
        });
    }

    public void showFileChooser(View view) {

        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*"))
                .withFilterDirectories(true)
                .withHiddenFiles(true)
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            try {
                readFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void readFile(String filePath) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader);

        String singleLine;
        int numberOfLines = 0;

        try {
            while((singleLine = br.readLine()) != null){
                numberOfLines++;
                Log.d(TAG,singleLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            br.close();
        }

    }
}
