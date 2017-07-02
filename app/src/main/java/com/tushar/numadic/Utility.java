package com.tushar.numadic;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;

/**
 * Created by Tushar Vengurlekar
 * Created on 02/07/17.
 */

public class Utility {

    public enum NetworkMode {
        WIFI,
        MOBILE,
        NO_NETWORK
    }

    /**
     * Calculates distance between 3 points using latitude and longitude
     *
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return distance between points
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    /**
     * Calculates percentage of battery available to consume
     *
     * @param context calling context
     * @return Battery status
     */
    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    /**
     * Evaluates current mode of network usage
     *
     * @param context calling context
     * @return network mode enum
     */
    public static NetworkMode getNetworkMode(Context context) {

        NetworkMode networkMode;
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = manager.getActiveNetworkInfo();
        final android.net.NetworkInfo mobile = manager.getActiveNetworkInfo();

        if (wifi == null && mobile == null) {
            networkMode = NetworkMode.NO_NETWORK;
        } else if (wifi != null && wifi.getType() == ConnectivityManager.TYPE_WIFI) {
            networkMode = NetworkMode.WIFI;
        } else if (mobile != null && mobile.getType() == ConnectivityManager.TYPE_MOBILE) {
            networkMode = NetworkMode.MOBILE;
        } else {
            networkMode = NetworkMode.NO_NETWORK;
        }
        return networkMode;
    }
}
