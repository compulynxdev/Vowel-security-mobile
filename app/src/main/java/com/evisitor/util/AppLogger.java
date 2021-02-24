package com.evisitor.util;

import android.util.Log;

import com.evisitor.BuildConfig;

/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class AppLogger {

    private AppLogger() {
        // This class is not publicly instantiable
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            Log.w(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            Log.v(tag, msg);
        }
    }

}
