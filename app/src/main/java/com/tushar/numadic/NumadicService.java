package com.tushar.numadic;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;


public class NumadicService extends IntentService {

    enum ServiceMode {
        Active,
        Idle
    }

    private ServiceMode serviceMode;

    public NumadicService() {
        super("NumadicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        System.out.println("Service Started");

        serviceMode = ServiceMode.Idle;

        locationScheduler(this);
        // healthScheduler();

    }

    private void locationScheduler(Context context) {

        System.out.println("locationScheduler called");

        Location location = new DataManager(context).getLocation();

//        if (location == null) {

        if (isValidated()) {
            trackLocation();

            if (evaluateServiceMode(location) == ServiceMode.Idle) {
                scheduleLocationTask(30);
            } else {
                scheduleLocationTask(2);
            }
        } else {
            if (evaluateServiceMode(location) == ServiceMode.Idle) {
                scheduleLocationTask(30);
            } else {
                scheduleLocationTask(2);
            }
        }
//        }

//        else {
//
//        }

        // 1 If last location is not known
        // 2 Find Location
        // 3 Decide Service Mode
        // 4 If Service Mode is Active
        // 5 Schedule next Itiration for 2 minutes
        // 6 If Service Mode is Idle
        // 7 Schedule next Itiration for 30 minutes
        // 8 If last location is known
        // 2 Find Location
        // 3 Decide Service Mode
        // 4 If Service Mode is Active
        // 5 Schedule next Itiration for 2 minutes
        // 6 If Service Mode is Idle
        // 5 Schedule next Itiration for 30 minutes
    }

    private ServiceMode evaluateServiceMode(Location location) {
        ServiceMode serviceMode = ServiceMode.Idle;

        // If your current speed is more than 5kmph
        String speeed = getCurrentSpeed();

        // If you have moved for more than 50m since last location data

        return serviceMode;
    }

    private String getCurrentSpeed() {
        return null;
    }

    private void healthScheduler() {

        // 2 Find Health
        // 5 Schedule next Itiration for 10 minutes
    }

    private boolean isValidated() {
        boolean success = true;

        return success;
    }

    private void scheduleHealthTask(int duration) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                healthScheduler();
            }
        }, 1000 * duration);
    }

    private void scheduleLocationTask(int duration) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationScheduler(NumadicService.this);
            }
        }, 1000 * duration);
    }

    private void trackLocation() {
        // Get Location
        // Save Location in the file;
    }

    private void trackHealth() {
        // Get Health Data
        // Save Health Data in a file
    }
}
