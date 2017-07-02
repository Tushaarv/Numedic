package com.tushar.numadic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView textServiceStatus, textServiceDuration;
    private Button buttonService;
    private Intent intent;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textServiceStatus = findViewById(R.id.main_text_service_status);
        textServiceDuration = findViewById(R.id.main_text_service_duration);
        buttonService = findViewById(R.id.main_button_start_service);

        dataManager = new DataManager(this);
        intent = new Intent(this, NumadicService.class);

    }

    /**
     * This listener will start the service if it is not running
     * If service is running it will stop it
     *
     * @param view
     */
    public void onClickButtonStartService(View view) {

        if (dataManager.getServiceStatus()) {

            serviceStop();
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                } else {

                    serviceStart();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dataManager.getServiceStatus()) {
            postServiceStart();

        } else {
            postServiceStop();
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

    /**
     * Starts Service
     */
    private void serviceStart() {
        startService(intent);
        dataManager.setServiceStatus(true);
        dataManager.setServiceStartTime(TimeManager.getCurrentUTCTime());
        postServiceStart();
    }

    /**
     * Processes after service is started
     */
    private void postServiceStart() {

        textServiceStatus.setText(R.string.service_status_running);
        textServiceStatus.setTextColor(Color.GREEN);

        String serviceStartTime = dataManager.getServiceStartTime();
        String duration = TimeManager.findTimeDifference(serviceStartTime);
        textServiceDuration.setText(duration);

        buttonService.setText(R.string.main_button_stop_service);
    }

    /**
     * Stops Service
     */
    private void serviceStop() {
        stopService(intent);
        dataManager.setServiceStatus(false);
        dataManager.setServiceStartTime(null);
        postServiceStop();
    }


    /**
     * Processes after service is stopped
     */
    private void postServiceStop() {

        textServiceStatus.setText(R.string.service_status_not_running);
        textServiceStatus.setTextColor(Color.RED);

        String serviceStartTime = dataManager.getServiceStartTime();
        String duration = TimeManager.findTimeDifference(serviceStartTime);
        textServiceDuration.setText(duration);

        buttonService.setText(R.string.main_button_start_service);
    }
}