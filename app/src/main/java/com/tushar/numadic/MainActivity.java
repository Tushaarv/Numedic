package com.tushar.numadic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            } else {
                startService(new Intent(this, NumadicService.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    startService(new Intent(this, NumadicService.class));
                } else {
                    // permission denied,
                    Toast.makeText(MainActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
