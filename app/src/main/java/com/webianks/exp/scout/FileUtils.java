package com.webianks.exp.scout;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by R Ankit on 22-02-2017.
 */

public class FileUtils {

    private static File filepath;

    public static boolean writeOutputFile(String content) {

        BufferedWriter bw = null;
        boolean success = false;

        try {
            bw = new BufferedWriter(new FileWriter(filepath, true));
            bw.write(content);
            bw.newLine();
            bw.flush();
            success = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            success = false;
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
            }
        }
        return success;
    }

    public static void createOutputFile(){

        String timestamp = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
        File root = new File(Environment.getExternalStorageDirectory(), "Appathon");
        if (!root.exists()) {
            root.mkdirs();
        }
        filepath = new File(root,"output_"+timestamp+".txt");
    }

    public static void deleteFile(){

        if (filepath!=null){
            boolean deleted = filepath.delete();
            if (deleted)
                Log.d("webi","Deleted older file.");
        }
    }

}
