package com.tushar.numadic;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;

import java.io.File;


public class NumadicService extends IntentService {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private enum ServiceMode {
        Active,
        Idle
    }

    private enum NetworkMode {
        WIFI,
        MOBILE,
        NO_NETWORK
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
        healthScheduler(this);

    }

    private void locationScheduler(Context context) {

        System.out.println("locationScheduler called");

        Location location = new DataManager(context).getLocation();
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
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);

            locationManager = null;
            locationListener = null;

        }

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(android.location.Location location) {
                location.getLatitude();
//                (sqrt((currentGPSPointX - lastGPSPointX)^2) + (currentGPSPointY - lastGPSPointY)^2)) / (time between GPS points)
//                (Math.sqrt(Math.pow(location.getLatitude() - location.getLongitude(), 2.0) + (currentGPSPointY - lastGPSPointY)^2)) / (time between GPS points)
                System.out.println("Current speed:" + location.getSpeed() + " And " + location.hasSpeed());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }

        //Read more: http://www.androidhub4you.com/2013/06/how-to-get-device-current-speed-in_112.html#ixzz4ldj3qXd5
        return null;
    }

    private void healthScheduler(Context context) {

        // 2 Find Health
        trackHealth(context);
        // 5 Schedule next Itiration for 10 minutes
        scheduleHealthTask(2);
    }

    private boolean isValidated() {
        boolean success = true;

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            GnssStatus gnssStatus = new GnssStatus();
//        }


        return success;
    }

    private void scheduleHealthTask(int duration) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                healthScheduler(NumadicService.this);
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

    private void trackHealth(Context context) {

        // Get Health Data
        String healthStatus = " ";

        // Battery Usage
        String batteryUsage = String.valueOf(getBatteryPercentage(NumadicService.this));
        System.out.println("Current Battery Level:" + batteryUsage);
        healthStatus = healthStatus + "Battery Status:" + batteryUsage;

        NetworkMode networkMode = checkNetworkStatus(context);

        if (networkMode == NetworkMode.NO_NETWORK) {
            System.out.println(" Network Status: Network Not Available");
            healthStatus = healthStatus + "\n Network Status: Network Not Available";
        } else if (networkMode == NetworkMode.WIFI) {
            System.out.println(" Network Status: WIFI Network");
            healthStatus = healthStatus + "\n Network Status: WIFI Network";

            // WIFI Network Details
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            String wifiNetworkId = wifiManager.getConnectionInfo().getSSID();
            String wifiSpeed = String.valueOf(wifiManager.getConnectionInfo().getRssi());

            System.out.println(" WIFI Network Id: " + wifiNetworkId);
            System.out.println(" WIFI Speed: " + wifiSpeed);

            healthStatus = healthStatus + "\n WIFI Network Id: " + wifiNetworkId;
            healthStatus = healthStatus + "\n WIFI Speed: " + wifiSpeed;


        } else if (networkMode == NetworkMode.MOBILE) {
            healthStatus = healthStatus + "\n Network Status: Mobile Network";
            System.out.println(" Network Status: Mobile Network");

            // Mobile Network Details
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();

            String mobileNetworkId = String.valueOf(telephonyManager.getNetworkOperatorName());
            String mobileNetworkSpeed = String.valueOf(cellSignalStrengthGsm.getDbm());


            System.out.println(" Mobile Network Id: " + mobileNetworkId);
            System.out.println(" Mobile Speed: " + mobileNetworkSpeed);

            healthStatus = healthStatus + "\n Mobile Network Id: " + mobileNetworkId;
            healthStatus = healthStatus + "\n Mobile Speed: " + mobileNetworkSpeed;
        }

        System.out.println("Combine Health Status:" + healthStatus);

        // Save Health Data in a file
        NFileManager fileManager = new NFileManager();
        File file = fileManager.getHealthFile(this);
        fileManager.writeToAFile(file, healthStatus);
    }


    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }


    private NetworkMode checkNetworkStatus(Context context) {

        NetworkMode networkMode;
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = manager.getActiveNetworkInfo();
        final android.net.NetworkInfo mobile = manager.getActiveNetworkInfo();

        if (wifi == null && mobile == null) {
            networkMode = NetworkMode.NO_NETWORK; // "noNetwork";
        } else if (wifi.getType() == ConnectivityManager.TYPE_WIFI) {
            networkMode = NetworkMode.WIFI; //"wifi";
        } else if (mobile.getType() == ConnectivityManager.TYPE_MOBILE) {
            networkMode = NetworkMode.MOBILE;// "mobileData";
        } else {
            networkMode = NetworkMode.NO_NETWORK; // "noNetwork";
        }
        return networkMode;
    }
}
