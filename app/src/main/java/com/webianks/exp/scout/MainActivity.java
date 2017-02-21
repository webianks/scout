package com.webianks.exp.scout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.webianks.exp.scout.model.NamedEntities;
import com.webianks.exp.scout.model.TypedRelations;
import com.webianks.exp.scout.network.ApiServices;
import com.webianks.exp.scout.network.RestClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private TextView selectedFile;
    private ProgressDialog progressDialog;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFile = (TextView) findViewById(R.id.selectedFile);

        //testCalls();

    }


    private void testCalls(String input, String model) {

        //Log.d(TAG,"QUERY: "+input);

        ApiServices apiService = new RestClient().getApiService();

        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities("PDFs from Ravi in the last 3 days", "json", model);
        Call<TypedRelations> typedRelationsCall = apiService.getTypedRelations("PDFs from Ravi in the last 3 days", "json", model);

        //asynchronous call
        namedEntitiesCall.enqueue(new Callback<NamedEntities>() {
            @Override
            public void onResponse(Call<NamedEntities> call, Response<NamedEntities> response) {

                if (response.isSuccessful()) {

                    NamedEntities namedEntities = response.body();

                    if (namedEntities.getEntities() != null && namedEntities.getEntities().size() > 0) {

                        for (NamedEntities.EntitiesBean entitiesBean : namedEntities.getEntities())
                            Log.d(TAG, entitiesBean.getText());
                    }
                    dismissDialogNow(++count);
                }
            }

            @Override
            public void onFailure(Call<NamedEntities> call, Throwable t) {
                dismissDialogNow(++count);
            }
        });

        typedRelationsCall.enqueue(new Callback<TypedRelations>() {
            @Override
            public void onResponse(Call<TypedRelations> call, Response<TypedRelations> response) {

                if (response.isSuccessful()) {

                    TypedRelations typedRelations = response.body();

                    if (typedRelations.getTypedRelations() != null && typedRelations.getTypedRelations().size() > 0) {

                        //Toast.makeText(MainActivity.this, "Got the relations", Toast.LENGTH_LONG).show();
                        Log.d(TAG, typedRelations.getTypedRelations().get(0).getType());

                    }
                    dismissDialogNow(++count);

                }
            }

            @Override
            public void onFailure(Call<TypedRelations> call, Throwable t) {
                dismissDialogNow(++count);
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
            String format = filePath.substring(filePath.lastIndexOf(".") + 1);
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            selectedFile.setText("Selected file: " + fileName);

            if (format.equals("txt"))
                askToExtractParameters(filePath);
            else
                Toast.makeText(this, getString(R.string.please_select), Toast.LENGTH_LONG).show();

        }
    }


    private void askToExtractParameters(final String filePath) {

        new AlertDialog.Builder(this)
                .setTitle("Extraction")
                .setMessage("Are you sure you want to use this file to extract keywords?")
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        getModelIdFromServer(filePath);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void getModelIdFromServer(final String filePath) {


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Model");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){

                    if (objects != null && objects.size()>0){

                        String model = objects.get(0).getString("model_id");

                        try {
                            readFile(filePath,model);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"Can't get model",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Can't get model",Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    private void readFile(String filePath,String model) throws IOException {

        showProgressDialog();

        FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader);

        String singleLine;
        int numberOfLines = 0;

        if (br.readLine() != null)
            testCalls(br.readLine(),model);

       /* try {
            while ((singleLine = br.readLine()) != null) {

                numberOfLines++;
                //Log.d(TAG,singleLine);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }*/

    }


    private void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setIndeterminate(true);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void dismissDialogNow(int count) {


        if (progressDialog != null && count == 2)
            progressDialog.dismiss();

    }

}
