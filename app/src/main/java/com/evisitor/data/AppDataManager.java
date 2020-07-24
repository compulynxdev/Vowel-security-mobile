package com.evisitor.data;

import android.app.Activity;
import android.content.Context;

import com.evisitor.data.local.prefs.AppPreferenceHelper;
import com.evisitor.data.remote.AppApiHelper;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppDataManager implements DataManager {

    private AppApiHelper apiHelper;
    private AppPreferenceHelper preferenceHelper;
    private static AppDataManager instance;

    private AppDataManager(Context context) {
        apiHelper = AppApiHelper.getAppApiInstance();
        preferenceHelper = new AppPreferenceHelper(context);
    }

    public static synchronized AppDataManager getInstance(Context context){
        if (instance==null){
            instance = new AppDataManager(context);
        }
        return instance;
    }

    @Override
    public boolean isLoggedIn() {
        return preferenceHelper.isLoggedIn();
    }

    @Override
    public void setLoggedIn(boolean isLoggedIn) {
        preferenceHelper.setLoggedIn(isLoggedIn);
    }

    @Override
    public void logout(Activity activity) {
        preferenceHelper.logout(activity);
    }

    @Override
    public String getLanguage() {
        return preferenceHelper.getLanguage();
    }

    @Override
    public void setLanguage(String language) {
        preferenceHelper.setLanguage(language);
    }

    @Override
    public String getUsername() {
        return preferenceHelper.getUsername();
    }

    @Override
    public void setUsername(String userName) {
        preferenceHelper.setUsername(userName);
    }

    @Override
    public String getUserPassword() {
        return preferenceHelper.getUserPassword();
    }

    @Override
    public void setUserPassword(String userPassword) {
        preferenceHelper.setUserPassword(userPassword);
    }

    @Override
    public String getAccessToken() {
        return preferenceHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String token) {
        preferenceHelper.setAccessToken(token);
    }

    @Override
    public String getUserId() {
        return preferenceHelper.getUserId();
    }

    @Override
    public void setUserId(String userId) {
        preferenceHelper.setUserId(userId);
    }
}
