package com.tushar.numadic;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Tushar Vengurlekar
 * Created on 01/07/17.
 */

public class TimeManager {


    public static String getCurrentUTCTime() {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        return dateFormat.format(new Date());
    }
}
