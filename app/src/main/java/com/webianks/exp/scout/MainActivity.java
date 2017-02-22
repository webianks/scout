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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private TextView selectedFile;
    private ProgressDialog progressDialog;
    private ArrayList<String> allInputs;
    private int count = 0;
    private int numberOfLines;
    private boolean fileSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFile = (TextView) findViewById(R.id.selectedFile);

        testCalls("From.pdf Ishita sent yesterday with size 10 mega bytes", "");

    }


    private void testCalls(final String input, final String model) {

        //Log.d(TAG,"QUERY: "+input);

        ApiServices apiService = new RestClient().getApiService();
        Call<NamedEntities> namedEntitiesCall = apiService.getNamedEntities(input, "json", model);

        //asynchronous call
        namedEntitiesCall.enqueue(new Callback<NamedEntities>() {
            @Override
            public void onResponse(Call<NamedEntities> call, Response<NamedEntities> response) {

                if (response.isSuccessful()) {

                    NamedEntities namedEntities = response.body();
                    OutputModel outputModel = new OutputModel();

                    if (namedEntities.getEntities() != null && namedEntities.getEntities().size() > 0) {

                        for (int i = 0; i < namedEntities.getEntities().size(); i++) {

                            NamedEntities.EntitiesBean entitiesBean = namedEntities.getEntities().get(i);
                            setAttachmentType(outputModel, entitiesBean);
                            setAttachmentSize(outputModel, entitiesBean);
                            setAttachmentName(outputModel, entitiesBean);
                            setFrom(outputModel, i, entitiesBean, namedEntities);
                            setToDate(outputModel, entitiesBean);
                            setFromDate(outputModel, entitiesBean);
                            setCC(outputModel, i, entitiesBean, namedEntities);
                            setTo(outputModel, i, entitiesBean, namedEntities);
                            setSubject(outputModel, i, entitiesBean, namedEntities);

                        }

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(input + "\n");
                        stringBuilder.append("From " + outputModel.getFrom() + "\n");
                        stringBuilder.append("To " + outputModel.getTo() + "\n");
                        stringBuilder.append("ToDate " + outputModel.getToDate() + "\n");
                        stringBuilder.append("FromDate " + outputModel.getFromDate() + "\n");
                        stringBuilder.append("HasAttachments " + outputModel.hasAttachments() + "\n");
                        stringBuilder.append("AttachmentType " + outputModel.getAttachmentType() + "\n");
                        stringBuilder.append("AttachmentSize " + outputModel.getAttachmentSize() + "\n");
                        stringBuilder.append("AttachmentName " + outputModel.getAttachmentName() + "\n");
                        stringBuilder.append("Subject " + outputModel.getSubject() + "\n");
                        stringBuilder.append("CC " + outputModel.getCC() + "\n\n");

                        Log.d(TAG, stringBuilder.toString());

                        //fileSuccess = FileUtils.writeOutputFile(stringBuilder.toString());

                    }
                    count++;
                    if (count < numberOfLines) {
                        testCalls(allInputs.get(count), model);
                        progressDialog.setProgress(count);
                    } else
                        dismissDialogNow();

                }
            }

            @Override
            public void onFailure(Call<NamedEntities> call, Throwable t) {

                count++;
                if (count < numberOfLines) {
                    testCalls(allInputs.get(count), model);
                    progressDialog.setProgress(count);
                } else
                    dismissDialogNow();
            }

        });

    }

    private void setSubject(OutputModel outputModel, int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("SUBJECT")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("SUBJECT_TITLE"))
                    outputModel.setSubject(nextEntity.getText());
            }

        }
    }

    private void setTo(OutputModel outputModel, int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("TO")) {

            if ((i + 1) < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setTo(nextEntity.getText());


                if ((i + 2) < namedEntities.getEntities().size()) {

                    if ((i + 3) < namedEntities.getEntities().size()){

                        NamedEntities.EntitiesBean ccEntry = namedEntities.getEntities().get(i + 3);
                        NamedEntities.EntitiesBean lastEntity = namedEntities.getEntities().get(i + 2);

                        if (!ccEntry.getType().equals("CC"))
                            outputModel.setTo(outputModel.getTo() + " and " + lastEntity.getText());
                    }else{

                        NamedEntities.EntitiesBean lastEntity = namedEntities.getEntities().get(i + 2);
                        if (lastEntity.getType().equals("USERNAME"))
                            outputModel.setTo(outputModel.getTo() + " and " + lastEntity.getText());
                    }

                }

            }

        }
    }

    private void setCC(OutputModel outputModel, int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("CC")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setCC(nextEntity.getText());
            }

            if (i-1 >= 0){

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i - 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setCC(nextEntity.getText());
            }
        }
    }

    private void setFromDate(OutputModel outputModel, NamedEntities.EntitiesBean entitiesBean) {

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

    private void setToDate(OutputModel outputModel, NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("SPAN"))
            outputModel.setToDate("Today"); //here only for last
    }

    private void setFrom(OutputModel outputModel, int i, NamedEntities.EntitiesBean entitiesBean, NamedEntities namedEntities) {

        if (entitiesBean.getType().equals("FROM")) {

            if (i + 1 < namedEntities.getEntities().size()) {

                NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i + 1);
                if (nextEntity.getType().equals("USERNAME"))
                    outputModel.setFrom(nextEntity.getText());

            }

            if ((i-1)>=0){

                NamedEntities.EntitiesBean previousEntity = namedEntities.getEntities().get(i-1);
                if (previousEntity.getType().equals("USERNAME") && entitiesBean.getText().contains("sent"))
                    outputModel.setFrom(previousEntity.getText());
            }

        }

        if (i == 0){

            NamedEntities.EntitiesBean nextEntity = namedEntities.getEntities().get(i);
            if (nextEntity.getType().equals("USERNAME"))
                outputModel.setFrom(nextEntity.getText());
        }

    }

    private void setAttachmentName(OutputModel outputModel, NamedEntities.EntitiesBean entitiesBean) {

        if (entitiesBean.getType().equals("ATTACHMENT_NAME"))
            outputModel.setAttachmentName(entitiesBean.getText());

    }

    private void setAttachmentSize(OutputModel outputModel, NamedEntities.EntitiesBean entitiesBean) {

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

    private void setAttachmentType(OutputModel outputModel, NamedEntities.EntitiesBean entitiesBean) {

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
        allInputs = new ArrayList<>();
        count = 0;
        fileSuccess = false;
        numberOfLines = 0;

        try {
            while ((singleLine = br.readLine()) != null) {
                numberOfLines++;
                allInputs.add(singleLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
            progressDialog.setMax(numberOfLines);
            FileUtils.createOutputFile();
            startExtracting(allInputs, model);
        }

    }

    private void startExtracting(ArrayList<String> allInputs, String model) {

        if (allInputs != null && allInputs.size() > 0)
            testCalls(allInputs.get(0), model);

    }


    private void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(numberOfLines);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void dismissDialogNow() {

        if (progressDialog != null)
            progressDialog.dismiss();

        if (fileSuccess)
            Toast.makeText(this, getString(R.string.file_output), Toast.LENGTH_LONG).show();

    }

}
