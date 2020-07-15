package com.evisitor.eVisitor.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppPreferenceHelper implements PreferenceHelper {

    private String APP_PREFERENCE = "APP_PREFERENCE";

    private final SharedPreferences mPrefs;

    public AppPreferenceHelper(Context context) {
        mPrefs = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }
}
