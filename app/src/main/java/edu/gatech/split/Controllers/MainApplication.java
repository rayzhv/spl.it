package edu.gatech.split.Controllers;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.*;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainApplication extends Application {

    public static MainApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        copyTessDataForTextRecognition();
    }

    private String tessDataPath() {
        return MainApplication.instance.getExternalFilesDir(null)+"/tessdata/";
    }

    public String getTessDataParentDirectory() {
        return MainApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyTessDataForTextRecognition() {
        Runnable run = new Runnable() {
            public void run() {
                AssetManager assetManager = MainApplication.instance.getAssets();
                OutputStream out = null;
                try {
                    InputStream in = assetManager.open("eng.traineddata");
                    String tesspath = instance.tessDataPath();
                    File tessFolder = new File(tesspath);
                    if (!tessFolder.exists()) {
                        tessFolder.mkdir();
                    }
                    String tessData = tesspath + "/" + "eng.traineddata";
                    File tessFile = new File(tessData);
                    if (!tessFile.exists()) {
                        out = new FileOutputStream(tessData);
                        byte[] buffer = new byte[1024];
                        int read = in.read(buffer);
                        while (read != -1) {
                            out.write(buffer, 0, read);
                            read = in.read(buffer);
                        }
                        Log.d("MainApplication", "Did not finish copying tess file");
                    } else {
                        Log.d("MainApplication", "Tess File exists");
                    }
                } catch (Exception e){
                    Log.d("MainApplication", "Could not copy with following error: " + e.toString());
                } finally {
                    try {
                        if (out != null)
                            out.close();
                    } catch (Exception e){
                    }
                }
            }
        };
        new Thread(run).start();
    }

}
