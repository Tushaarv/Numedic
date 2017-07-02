package com.tushar.numadic;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Tushar Vengurlekar
 * Created on 01/07/17.
 */

class TimeManager {


    static String getCurrentUTCTime() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        return dateFormat.format(new Date());
    }

    static String findTimeDifference(String time) {

        if (time == null) {
            return "";
        }

        String minutes;
        String hours;

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));

        Date dateOld = null;

        try {
            dateOld = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateNew = new Date();


        if (dateOld != null) {

            long diff = dateNew.getTime() - dateOld.getTime();
            double diffMinutes = diff / (60 * 1000);
            minutes = String.valueOf(Math.round(diffMinutes % 60));
            hours = String.valueOf(Math.round(diffMinutes / 60));
        } else {
            minutes = "0";
            hours = "0";
        }
        return "Hours: " + hours + ", Mins: " + minutes;
    }
}
