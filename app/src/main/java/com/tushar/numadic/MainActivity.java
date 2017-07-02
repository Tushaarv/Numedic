package com.tushar.numadic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
//    int data = 0;
//    int location = 0;
//
//    NFileManager fileManager = new NFileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButtonStartService(View view) {
        //TODO Start The Service

//        File file = fileManager.getHealthFile(this);
//        fileManager.writeToAFile(file, "Data: " + data);
//        data++;
//
//        file = fileManager.getLocationFile(this);
//        fileManager.writeToAFile(file, "Location: " + location);
//        location++;

//        startService(new Intent(this, NumadicService.class));

//        if (isMyServiceRunning(NumadicService.class)) {
//            Toast.makeText(getBaseContext(), "Service is already running", Toast.LENGTH_SHORT).show();
//        } else {
//
//            Toast.makeText(getBaseContext(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
            startService(new Intent(this, NumadicService.class));
//        }
    }


//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
