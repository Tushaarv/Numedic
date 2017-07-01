package com.tushar.numadic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends Activity {
    int data = 0;
    int location = 0;

    NFileManager fileManager = new NFileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButtonStartService(View view) {
        //TODO Start The Service

        File file = fileManager.getHealthFile(this);
        fileManager.writeToAFile(file, "Data: " + data);
        data++;

        file = fileManager.getLocationFile(this);
        fileManager.writeToAFile(file, "Location: " + location);
        location++;

    }
}
