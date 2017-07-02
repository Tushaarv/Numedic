package com.tushar.numadic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView textServiceStatus, textServiceDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textServiceStatus = findViewById(R.id.main_text_service_status);
        textServiceDuration = findViewById(R.id.main_text_service_duration);
    }

    public void onClickButtonStartService(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            } else {
                serviceStart();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DataManager dataManager = new DataManager(this);
        boolean isServiceRunning = dataManager.getServiceStatus();

        if (isServiceRunning) {
            textServiceStatus.setText(R.string.service_status_running);
            textServiceStatus.setTextColor(Color.GREEN);

            String serviceStartTime = dataManager.getServiceStartTime();
            String duration = TimeManager.findTimeDifference(serviceStartTime);
            textServiceDuration.setText(duration);

        } else {
            textServiceStatus.setText(R.string.service_status_not_running);
            textServiceStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted,
                    serviceStart();
                } else {
                    // Permission denied,
                    Toast.makeText(MainActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void serviceStart() {
        startService(new Intent(this, NumadicService.class));

        DataManager dataManager = new DataManager(this);
        dataManager.setServiceStatus(true);
        dataManager.setServiceStartTime(TimeManager.getCurrentUTCTime());
        textServiceStatus.setText(R.string.service_status_running);
        textServiceStatus.setTextColor(Color.GREEN);
    }
}


//TODO Start The Service

//    int data = 0;
//    int location = 0;
//
//    NFileManager fileManager = new NFileManager();
//        File file = fileManager.getHealthFile(this);
//        fileManager.writeToAFile(file, "Data: " + data);
//        data++;
//
//        file = fileManager.getLocationFile(this);
//        fileManager.writeToAFile(file, "LocationData: " + location);
//        location++;

//        startService(new Intent(this, NumadicService.class));

//        if (isMyServiceRunning(NumadicService.class)) {
//            Toast.makeText(getBaseContext(), "Service is already running", Toast.LENGTH_SHORT).show();
//        } else {
//
//            Toast.makeText(getBaseContext(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
//        }
