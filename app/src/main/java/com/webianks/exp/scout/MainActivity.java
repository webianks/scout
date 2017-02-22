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
import com.webianks.exp.scout.model.OutputModel;
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
    private OutputModel outputModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFile = (TextView) findViewById(R.id.selectedFile);

        testCalls(" ", "");

    }


    private void testCalls(String input, String model) {

        //Log.d(TAG,"QUERY: "+input);

        ApiServices apiService = new RestClient().getApiService();
        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities("mail sent to me on date 3 Jan 2015", "json", model);

        //asynchronous call
        namedEntitiesCall.enqueue(new Callback<NamedEntities>() {
            @Override
            public void onResponse(Call<NamedEntities> call, Response<NamedEntities> response) {

                if (response.isSuccessful()) {

                    NamedEntities namedEntities = response.body();
                    outputModel = new OutputModel();

                    if (namedEntities.getEntities() != null && namedEntities.getEntities().size() > 0) {

                        for (int i = 0; i < namedEntities.getEntities().size(); i++) {

                            NamedEntities.EntitiesBean entitiesBean = namedEntities.getEntities().get(i);
                            setAttachmentType(entitiesBean);
                            setAttachmentSize(entitiesBean);
                            setAttachmentName(entitiesBean);
                            setFrom(i,entitiesBean,namedEntities);
                            setToDate(entitiesBean);
                            setFromDate(entitiesBean);
                            setCC(i,entitiesBean,namedEntities);
                            setTo(i,entitiesBean,namedEntities);
                            setSubject(i,entitiesBean,namedEntities);

                        }

                        Log.d(TAG,"mail sent to me on date 3 Jan 2015");
                        Log.d(TAG, "FROM " + outputModel.getFrom());
                        Log.d(TAG, "To " + outputModel.getTo());
                        Log.d(TAG, "ToDate " + outputModel.getToDate());
                        Log.d(TAG, "FromDate " + outputModel.getFromDate());
                        Log.d(TAG, "HasAttachments " + outputModel.hasAttachments());
                        Log.d(TAG, "AttachmentType " + outputModel.getAttachmentType());
                        Log.d(TAG, "AttachmentSize " + outputModel.getAttachmentSize());
                        Log.d(TAG, "AttachmentName " + outputModel.getAttachmentName());
                        Log.d(TAG, "Subject " + outputModel.getSubject());
                        Log.d(TAG, "CC " + outputModel.getCC());

                    }
                    dismissDialogNow(++count);
                }
            }

            @Override
            public void onFailure(Call<NamedEntities> call, Throwable t) {
                dismissDialogNow(++count);
            }
        });

    }

    private void setSubject(int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("SUBJECT")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("SUBJECT_TITLE"))
                    outputModel.setSubject(nextEntity.getText());
            }

        }
    }

    private void setTo(int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("TO")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setTo(nextEntity.getText());


                if (i + 2 < namedEntities.getEntities().size()) {
                    NamedEntities.EntitiesBean lastEntity = namedEntities.getEntities().get(i + 2);
                    if (nextEntity.getType().equals("USERNAME"))
                        outputModel.setTo(outputModel.getTo() + " and " + lastEntity.getText());
                }
            }

        }
    }

    private void setCC(int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("CC")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setCC(nextEntity.getText());
            }
        }
    }

    private void setFromDate(NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("DATE")) {

            //logic for days
            if (entitiesBean.getText().contains("day") || entitiesBean.getText().contains("Day")) {

                String[] splittedDay = entitiesBean.getText().split(" ");
                int date = 1;

                if (splittedDay.length > 1)
                    date = Integer.valueOf(splittedDay[0]);

                outputModel.setFromDate(outputModel.getToDate() + "-" + date);

            }
            //logic for months
            else if (entitiesBean.getText().contains("month") || entitiesBean.getText().contains("Month")) {

                String[] splittedMonth = entitiesBean.getText().split(" ");
                int day = 1;
                if (splittedMonth.length > 1)
                    day = Integer.valueOf(splittedMonth[0]);
                day = day * 30;

                outputModel.setFromDate(outputModel.getToDate() + "-" + day);


            }
            //logic for years
            else if (entitiesBean.getText().contains("year") || entitiesBean.getText().contains("Year")) {

                String[] splittedMonth = entitiesBean.getText().split(" ");
                int day = 1;
                if (splittedMonth.length > 1)
                    day = Integer.valueOf(splittedMonth[0]);
                day = day * 365;

                outputModel.setFromDate(outputModel.getToDate() + "-" + day);

            }
            //logic for specific date
            else {
                outputModel.setFromDate(entitiesBean.getText());
                outputModel.setToDate(entitiesBean.getText());
            }

        }
    }

    private void setToDate(NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("SPAN"))
            outputModel.setToDate("Today"); //here only for last
    }

    private void setFrom(int i, NamedEntities.EntitiesBean entitiesBean,NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("FROM")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setFrom(nextEntity.getText());

            }

        }
    }

    private void setAttachmentName(NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("ATTACHMENT_NAME"))
            outputModel.setAttachmentName(entitiesBean.getText());

    }

    private void setAttachmentSize(NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("ATTACHMENT_SIZE")) {
/*
                                if (entitiesBean.getText().contains("larger") || entitiesBean.getText().contains("Larger"))
                                    outputModel.setAttachmentSize(entitiesBean.getText().replace("larger than",">"));
                                else if(entitiesBean.getText().contains("smaller") || entitiesBean.getText().contains("Smaller"))
                                    outputModel.setAttachmentSize(entitiesBean.getText().replace("smaller than","<"));
                                else*/
            outputModel.setAttachmentSize(entitiesBean.getText());

        }
    }

    private void setAttachmentType(NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("ATTACHMENT_TYPE")) {

            if (!entitiesBean.getText().equalsIgnoreCase("Attachments"))
                outputModel.setAttachmentType(entitiesBean.getText());

            outputModel.setHasAttachments("YES");
        }
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


        showProgressDialog();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Model");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects != null && objects.size() > 0) {

                        String model = objects.get(0).getString("model_id");

                        try {
                            readFile(filePath, model);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Can't get model", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Can't get model", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void readFile(String filePath, String model) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader);

        String singleLine;
        int numberOfLines = 0;

        if (br.readLine() != null)
            testCalls(br.readLine(), model);

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
