package com.evisitor.util;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class CalenderUtils {
    private static final String TAG = "CalenderUtils";

    public static final String TIMESTAMP_FORMAT = "dd/MM/yyyy, hh:mm a";
    public static final String TIMESTAMP_FORMAT_24 = "dd/MM/yyyy HH:mm:ss";
    public static final String CUSTOM_TIMESTAMP_FORMAT_SLASH = "dd/MM/yyyy";
    public static final String CUSTOM_TIMESTAMP_FORMAT_AM = "EEE MMM, dd hh:mm a";
    public static final String CUSTOM_TIMESTAMP_FORMAT_DASH = "yyyy-MM-dd";
    public static final String DB_TIMESTAMP_FORMAT_DASH = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String TIME_FORMAT_SEC = "HH:mm:ss";
    public static final String TIME_FORMAT_AM = "hh:mm a";
    public static final String DAY = "EEE";
    public static final String DATE_FORMAT = "EEE, MMMM dd";
    //public static final String SERVER_DATE_FORMAT = "E MMM dd HH:mm:ss Z yyyy";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String SERVER_DATE_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    //public static final String CUSTOM_TIMESTAMP_FORMAT = "dd MMMM, yyyy";
    private static final String DB_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";


    private CalenderUtils() {
        // This class is not publicly instantiable
    }

    public static String getTimestamp(String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date());
    }

    public static String getTimestamp() {
        return String.valueOf(new Date().getTime());
    }

    public static String formatDate(String date, @NonNull String pFormat, @NonNull String dFormat) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat(pFormat, Locale.US);
            SimpleDateFormat displayFormat = new SimpleDateFormat(dFormat, Locale.US);
            parseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dTime = parseFormat.parse(date);
            assert dTime != null;
            return displayFormat.format(dTime);
        } catch (Exception e) {
            AppLogger.w(TAG, e.toString());
            return "";
        }
    }

    public static String formatDateWithOutUTC(String date, @NonNull String pFormat, @NonNull String dFormat) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat(pFormat, Locale.US);
            SimpleDateFormat displayFormat = new SimpleDateFormat(dFormat, Locale.US);
            Date dTime = parseFormat.parse(date);
            assert dTime != null;
            return displayFormat.format(dTime);
        } catch (Exception e) {
            AppLogger.w(TAG, e.toString());
            return "";
        }
    }

    public static String formatDate(Date date, @NonNull String dFormat) {
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat(dFormat, Locale.getDefault());
            return displayFormat.format(date);
        } catch (Exception e) {
            AppLogger.w(TAG, e.toString());
            return "";
        }
    }

    public static Date getDateFormat(String date, @NonNull String format) {
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat(format, Locale.getDefault());
            parseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return parseFormat.parse(date);
        } catch (Exception e) {
            AppLogger.w(TAG, e.toString());
            return null;
        }
    }

    public static Long getTimerDifference(String timer, String myTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(CalenderUtils.DB_TIMESTAMP_FORMAT, Locale.getDefault());

        try {
            Date startDate = formatter.parse(timer);

            Date endDate = formatter.parse(myTime);

            assert startDate != null;
            assert endDate != null;
            long diffInMilliSec = endDate.getTime() - startDate.getTime();

            return (diffInMilliSec / (1000 * 60)) % 60;
        } catch (ParseException e) {
            AppLogger.w(TAG, e.toString());
            return null;
        }
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    @NonNull
    public static String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return (day + "/" + month + "/" + year);

    }

    @NonNull
    public static String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return (hour + ":" + minute);
    }

    public static boolean isDateBefore(Date myDate) {
        Date curDate = new Date();
        if (myDate.before(curDate)) {
            return true;
        } else if (myDate.equals(curDate)) {
            return false;
        } else return false;
    }

    public static String getElapseTime(Date hostCheckOut, Date checkOut) {
        long timeDiff = checkOut.getTime() - hostCheckOut.getTime();
        long timeDiffInMin = timeDiff / (60 * 1000);
        long quotient = timeDiffInMin / 60;
        long remainder = timeDiffInMin % 60;
        return quotient + " hours " + remainder + " minutes";
    }

}
