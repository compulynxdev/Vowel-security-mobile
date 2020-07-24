package com.evisitor.data.local.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.evisitor.ui.login.LoginActivity;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppPreferenceHelper implements PreferenceHelper {

    private static final String APP_PREFERENCE = "APP_PREFERENCE";
    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";

    private static final String USER_NAME = "USER_NAME";
    private static final String USER_ID = "USER_ID";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String PREF_KEY_APP_LANGUAGE = "PREF_KEY_APP_LANGUAGE";

    private final SharedPreferences mPrefs;

    public AppPreferenceHelper(Context context) {
        mPrefs = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLoggedIn() {
        return mPrefs.getBoolean(PREF_KEY_USER_LOGGED_IN_MODE, false);
    }

    @Override
    public void setLoggedIn(boolean isLoggedIn) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, isLoggedIn).apply();
    }

    @Override
    public void logout(Activity activity) {
        setLoggedIn(false);
        Intent intent = LoginActivity.newIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public String getLanguage() {
        return null;
    }

    @Override
    public void setLanguage(String language) {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void setUsername(String userName) {

    }

    @Override
    public String getUserPassword() {
        return null;
    }

    @Override
    public void setUserPassword(String userPassword) {

    }

    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(String token) {

    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public void setUserId(String userId) {

    }
}
