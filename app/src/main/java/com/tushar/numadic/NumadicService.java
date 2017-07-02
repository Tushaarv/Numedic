package com.tushar.numadic;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

import static com.tushar.numadic.Utility.calculateDistance;
import static com.tushar.numadic.Utility.getBatteryPercentage;


public class NumadicService extends Service {

    private static final String DEBUG_TAG = "NumadicService";

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Handler locationHandler;
    private Handler healthHandler;

    private Runnable locationRunnable;
    private Runnable healthRunnable;

    private enum ServiceMode {
        Active,
        Idle
    }

    private int currentLocationDuration = 30;

    private ServiceMode serviceMode;

    @Override
    public void onDestroy() {

        if (locationHandler != null) {
            locationHandler.removeCallbacks(locationRunnable);
        }
        if (healthHandler != null) {
            healthHandler.removeCallbacks(healthRunnable);
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceMode = ServiceMode.Idle;

        locationScheduler();
        healthScheduler(this);

        return super.onStartCommand(intent, flags, startId);

    }

    private void locationScheduler() {

        if (isValidated()) {
            trackLocation();
        }
    }

    private boolean isValidated() {
        boolean success = true;

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            GnssStatus gnssStatus = new GnssStatus();
//        }
        return success;
    }


    /**
     * Evaluates Service Modes
     * Active Mode if :
     * 1. If current speed is more than 5 KMPH OR
     * 2. if user has moved for more than 50m since last location
     * Idle Otherwise
     *
     * @param distance distance travelled since last location capture
     * @param velocity current velocity calculated based on last location
     * @return applicable service mode
     */
    private ServiceMode evaluateServiceMode(double distance, double velocity) {
        ServiceMode serviceMode;

        if (velocity > 5 || distance * 1000 > 50) {
            serviceMode = ServiceMode.Active;
        } else {
            serviceMode = ServiceMode.Idle;
        }

        return serviceMode;
    }


    private void scheduleLocationTask(int duration) {

        locationHandler = new Handler();
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                locationScheduler();
            }
        };

        locationHandler.postDelayed(locationRunnable, 1000 * duration);
    }

    private void trackLocation() {

        final LocationData lastLocation = new DataManager(this).getLocation();

        // Clear previous instance as precaution if it is not cleared
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                double velocityValue = 0;

                if (lastLocation != null) {

                    double distance = calculateDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude());
                    double timeValue = currentLocationDuration;
                    velocityValue = distance / timeValue;

                    locationManager.removeUpdates(locationListener);

                    evaluateServiceMode(distance, velocityValue);
                    if (serviceMode == ServiceMode.Active) {
                        scheduleLocationTask(2);
                    } else {
                        scheduleLocationTask(30);
                    }
                } else {
                    scheduleLocationTask(30);
                }

                new DataManager(NumadicService.this).setLocation(new LocationData(location.getLatitude(), location.getLongitude()));
                locationManager.removeUpdates(locationListener);

                // Save LocationData in the file;
                NFileManager fileManager = new NFileManager();
                File file = fileManager.getLocationFile(NumadicService.this);

                String locationValue = "Latitude: " + location.getLatitude() + " Longitude : " + location.getLongitude() + " UTC Time: " + TimeManager.getCurrentUTCTime() + " Velocity :"
                        + velocityValue + " Accuracy :" + location.getAccuracy();

                Log.d(DEBUG_TAG, locationValue);
                fileManager.writeToAFile(file, locationValue);
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

                // This check is done just to satisfy android
                // Location permissions are granted in activity itself
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }


    private void healthScheduler(Context context) {

        // Find Health
        trackHealth(context);
        // Schedule next Iteration after 10 minutes
        scheduleHealthTask(10);
    }

    private void scheduleHealthTask(int duration) {

        healthHandler = new Handler();
        healthRunnable = new Runnable() {
            @Override
            public void run() {
                healthScheduler(NumadicService.this);
            }
        };

        healthHandler.postDelayed(healthRunnable, 1000 * (duration * 60));
    }

    private void trackHealth(Context context) {

        // Get Health Data
        String healthStatus = " ";
        healthStatus = healthStatus + "\n " + getBatteryUsageData();

        // Network Usage
        healthStatus = healthStatus + "\n " + getNetworkUsageData();

        // Save Health Data in a file
        NFileManager fileManager = new NFileManager();
        File file = fileManager.getHealthFile(this);
        fileManager.writeToAFile(file, healthStatus);
    }

    /**
     * Retrieves battery usage data
     *
     * @return
     */
    private String getBatteryUsageData() {

        String batteryUsage = String.valueOf(getBatteryPercentage(NumadicService.this));
        Log.d(DEBUG_TAG, "Current Battery Level:" + batteryUsage);
        return "Battery Status:" + batteryUsage;
    }

    /**
     * Retrieves network usage data
     */
    private String getNetworkUsageData() {

        Utility.NetworkMode networkMode = Utility.getNetworkMode(NumadicService.this);
        if (networkMode == Utility.NetworkMode.NO_NETWORK) {
            Log.d(DEBUG_TAG, " Network Status: Network Not Available");
            return "Network Status: Network Not Available";
        } else if (networkMode == Utility.NetworkMode.WIFI) {

            // WIFI Network Details
            WifiManager wifiManager = (WifiManager) NumadicService.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            String status = " Network Status: WIFI Network, Network Id: " + wifiManager.getConnectionInfo().getSSID() + ", Speed: " + wifiManager.getConnectionInfo().getRssi();

            Log.d(DEBUG_TAG, status);
            return status;

        } else if (networkMode == Utility.NetworkMode.MOBILE) {

            // Mobile Network Details
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
            CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();

            String status = " Network Status: Mobile Network, Network Id: " + String.valueOf(telephonyManager.getNetworkOperatorName()) + ", Speed: " + String.valueOf(cellSignalStrengthGsm.getDbm());

            Log.d(DEBUG_TAG, status);
            return status;
        } else {
            Log.d(DEBUG_TAG, " Network Status: Network Not Available");
            return "Network Status: Network Not Available";
        }
    }
}
