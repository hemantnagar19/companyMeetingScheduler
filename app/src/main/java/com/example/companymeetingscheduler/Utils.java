package com.example.companymeetingscheduler;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getCurrentDate() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String timestamp = simpleDateFormat.format(date);
        Log.d("az", "current date :" + timestamp);
        return timestamp;
    }

    public static String getCurrentDateLandscape() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String timestamp = simpleDateFormat.format(date);
        Log.d("az", "current date :" + timestamp);
        return timestamp;
    }

    public static String getCurrentDateSlash() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String timestamp = simpleDateFormat.format(date);
        Log.d("az", "current date slash:" + timestamp);
        return timestamp;
    }

    public static String getNextDateSlash(String dateValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = simpleDateFormat.parse(dateValue);
            long timeInMilliSec = date.getTime();
            timeInMilliSec = timeInMilliSec + 86400000;
            Date newDate = new Date(timeInMilliSec);
            String timestamp = simpleDateFormat.format(newDate);
            Log.d("az", "next date slash:" + timestamp);
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return "-1";
    }

    public static String getNextDate(String dateValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = simpleDateFormat.parse(dateValue);
            long timeInMilliSec = date.getTime() + 86400000;
            Date newDate = new Date(timeInMilliSec);
            String timestamp = simpleDateFormat.format(newDate);
            Log.d("az", "next date :" + timestamp);
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return "-1";
    }


    public static String getPrevDateSlash(String dateValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = simpleDateFormat.parse(dateValue);
            long timeInMilliSec = date.getTime();
            timeInMilliSec = timeInMilliSec - 86400000;
            Date newDate = new Date(timeInMilliSec);
            String timestamp = simpleDateFormat.format(newDate);
            Log.d("az", "prev date slash :" + timestamp);
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return "-1";
    }

    public static String getPrevDate(String dateValue) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = simpleDateFormat.parse(dateValue);
            long timeInMilliSec = date.getTime();
            timeInMilliSec = timeInMilliSec - 86400000;
            String timestamp = simpleDateFormat.format(timeInMilliSec);
            Log.d("az", "prev date :" + timestamp);
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return "-1";
    }

    public static String dateConversion(String dateValue, boolean slash) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = null;
            if (slash) {
                date = simpleDateFormat.parse(dateValue);
            } else {
                date = simpleDateFormat1.parse(dateValue);
            }

            long timeInMilliSec = date.getTime();
            Date newDate = new Date(timeInMilliSec);
            String timestamp = "";
            if (slash) {
                timestamp = simpleDateFormat1.format(newDate);
            } else {
                timestamp = simpleDateFormat.format(newDate);
            }
            Log.d("az", "convert date :" + timestamp);
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return "-1";
    }

    public static String convertTimeIntoAMPM(String time) {
        String suffix = " AM";
        String []timeArray = time.split(":");
        int timehh = Integer.parseInt(timeArray[0]);
        if ( timehh > 12) {
            timehh = timehh - 12;
            suffix = " PM";
        }
        return timehh + ":" + timeArray[1] + suffix;
    }

    public static String getCurrentDateTimeStamp() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timestamp = simpleDateFormat.format(date);
        Log.d("az", "current date time stamp :" + timestamp);
        return timestamp;
    }

    public static boolean isPastDate(String dateValue) {
        String currentDateValue = getCurrentDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date currentDate = simpleDateFormat.parse(currentDateValue);
            long currentTimeMillis = currentDate.getTime();
            Date date = simpleDateFormat.parse(dateValue);
            long timeInMillis = date.getTime();

            if (timeInMillis < currentTimeMillis) {
                return true;
            } else {
                return  false;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return false;
    }
}
