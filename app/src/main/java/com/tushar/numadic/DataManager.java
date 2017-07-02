package com.tushar.numadic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tushar Vengurlekar
 * Created on 01/07/17.
 */

public class DataManager {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final int PRIVATE_MODE = 0;

    // Shared Preferences
    private SharedPreferences preferences;

    // Shared Preferences file name
    private static final String PREF_NAME = Package.class + "_SP";

    DataManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    /**
     * Returns true if there is a file present for a specified file mode
     * returns false otherwise
     *
     * @param filePrefix Prefix of file used as key
     * @return boolean for presence of absence
     */
    boolean fileExists(String filePrefix) {
        return getActiveFileSuffix(filePrefix) != null;
    }

    /**
     * Gets active file's suffix for specified file mode.
     *
     * @param filePrefix Prefix of file used as key
     * @return file suffix if present, null otherwise
     */
    String getActiveFileSuffix(String filePrefix) {
        return preferences.getString(filePrefix, null);
    }

    /**
     * Sets active file's suffix for specified file mode.
     *
     * @param filePrefix Prefix of file used as key
     * @param fileSuffix Suffix of file
     */
    void setActiveFileSuffix(String filePrefix, String fileSuffix) {
        preferences.edit().putString(filePrefix, fileSuffix).apply();
    }

    public Location getLocation() {
        String latitude = preferences.getString(LATITUDE, null);
        String longitude = preferences.getString(LONGITUDE, null);

        if (latitude == null || longitude == null) {
            return null;
        } else {
            return new Location(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
    }

    public void setLocation(Location location) {
        preferences.edit().putString(LATITUDE, String.valueOf(location.getLatitude())).apply();
        preferences.edit().putString(LONGITUDE, String.valueOf(location.getLongitude())).apply();
    }
}
