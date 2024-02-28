package com.evisitor.data.local.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.evisitor.data.model.UserDetail;
import com.evisitor.ui.login.LoginActivity;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppPreferenceHelper implements PreferenceHelper {

    private static final String APP_PREFERENCE = "APP_PREFERENCE";
    private static final String PREF_KEY_USER_LOGGED_IN = "PREF_KEY_USER_LOGGED_IN";
    private static final String PREF_KEY_USER_REMEMBER = "PREF_KEY_USER_REMEMBER";

    private static final String USER_NAME = "USER_NAME";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String ACC_ID = "ACC_ID";
    private static final String USER_ID = "USER_ID";
    private static final String USER_PANIC_ID = "USER_PANIC_ID";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String PREF_KEY_APP_LANGUAGE = "PREF_KEY_APP_LANGUAGE";

    /*User Detail*/
    private static final String USER_FULL_NAME = "USER_FULL_NAME";
    private static final String USER_MASTER_ID = "USER_MASTER_ID";
    private static final String USER_PLATE_RECOGNIZER_API_KEY = "USER_PLATE_RECOGNIZER_API_KEY";
    private static final String USER_IMAGE = "USER_IMAGE";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String USER_COUNTRY = "USER_COUNTRY";
    private static final String USER_GENDER = "USER_GENDER";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_CONTACT = "USER_CONTACT";
    private static final String USER_DIALING_CODE = "USER_DIALING_CODE";
    private static final String IDENTIFY_FEATURE = "IDENTIFY_FEATURE";
    private static final String LEVEL_NAME = "LEVEL_NAME";
    private static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String COMMERCIAL = "COMMERCIAL";
    private static final String PROPERTY_COUNTRY = "PROPERTY_COUNTRY";
    private static final String PROPERTY_DIALING_CODE = "PROPERTY_DIALING_CODE";
    private static final String CHECK_OUT_FEATURE = "CHECK_OUT_FEATURE";
    private static final String PRINT_LABEL = "PRINT_LABEL";
    private static final String CAPTURE_VEHICLE_MODEL = "CAPTURE_VEHICLE_MODEL";
    private static final String PRINTER_ADDRESS = "PRINTER_ADDRESS";

    private final SharedPreferences mPrefs;

    public AppPreferenceHelper(Context context) {
        mPrefs = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLoggedIn() {
        return mPrefs.getBoolean(PREF_KEY_USER_LOGGED_IN, false);
    }

    @Override
    public void setLoggedIn(boolean isLoggedIn) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN, isLoggedIn).apply();
    }

    @Override
    public boolean isCheckOutFeature() {
        return mPrefs.getBoolean(CHECK_OUT_FEATURE, false);
    }

    @Override
    public void setCheckOutFeature(boolean checkOutFeature) {
        mPrefs.edit().putBoolean(CHECK_OUT_FEATURE, checkOutFeature).apply();
    }

    @Override
    public boolean isRememberMe() {
        return mPrefs.getBoolean(PREF_KEY_USER_REMEMBER, false);
    }

    @Override
    public void setRememberMe(boolean isRemember) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_REMEMBER, isRemember).apply();
    }

    @Override
    public String getLanguage() {
        return mPrefs.getString(PREF_KEY_APP_LANGUAGE, "English");
    }

    @Override
    public void setLanguage(String language) {
        mPrefs.edit().putString(PREF_KEY_APP_LANGUAGE, language).apply();
    }

    @Override
    public String getUsername() {
        return mPrefs.getString(USER_NAME, "");
    }

    @Override
    public void setUsername(String userName) {
        mPrefs.edit().putString(USER_NAME, userName).apply();
    }

    @Override
    public String getUserPassword() {
        return mPrefs.getString(USER_PASSWORD, "");
    }

    @Override
    public void setUserPassword(String userPassword) {
        mPrefs.edit().putString(USER_PASSWORD, userPassword).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(ACCESS_TOKEN, "");
    }

    @Override
    public void setAccessToken(String token) {
        mPrefs.edit().putString(ACCESS_TOKEN, token).apply();
    }

    @Override
    public String getAccountId() {
        return mPrefs.getString(ACC_ID, "");
    }

    @Override
    public void setAccountId(String accId) {
        mPrefs.edit().putString(ACC_ID, accId).apply();
    }

    @Override
    public String getUserId() {
        return mPrefs.getString(USER_ID, "-1");
    }

    @Override
    public void setUserId(String userId) {
        mPrefs.edit().putString(USER_ID, userId).apply();
    }

    @Override
    public String getAccountName() {
        return mPrefs.getString(ACCOUNT_NAME, "");
    }

    @Override
    public void setAccountName(String accountName) {
        mPrefs.edit().putString(ACCOUNT_NAME, accountName).apply();
    }

    @Override
    public UserDetail getUserDetail() {
        UserDetail userDetail = new UserDetail();
        userDetail.setId(mPrefs.getInt(USER_PANIC_ID,-1));
        userDetail.setUsername(getUsername());
        userDetail.setImageUrl(mPrefs.getString(USER_IMAGE, ""));
        userDetail.setFullName(mPrefs.getString(USER_FULL_NAME, ""));
        userDetail.setUserMasterId(mPrefs.getInt(USER_MASTER_ID, 0));
        userDetail.setApiKey(mPrefs.getString(USER_PLATE_RECOGNIZER_API_KEY, "a9d83735e6a2668030950af73b3595d9d0c4ad64"));
        userDetail.setEmail(mPrefs.getString(USER_EMAIL, ""));
        userDetail.setGender(mPrefs.getString(USER_GENDER, ""));
        userDetail.setContactNo(mPrefs.getString(USER_CONTACT, ""));
        userDetail.setDialingCode(mPrefs.getString(USER_DIALING_CODE, ""));
        userDetail.setAddress(mPrefs.getString(USER_ADDRESS, ""));
        userDetail.setCountry(mPrefs.getString(USER_COUNTRY, ""));
        userDetail.setGroupName(mPrefs.getString(GROUP_NAME, ""));
        return userDetail;
    }

    @Override
    public void setUserDetail(UserDetail userDetail) {
        mPrefs.edit().putString(USER_FULL_NAME, userDetail.getFullName()).apply();
        mPrefs.edit().putString(USER_IMAGE, userDetail.getImageUrl()).apply();
        mPrefs.edit().putString(USER_PLATE_RECOGNIZER_API_KEY, userDetail.getApiKey()).apply();
        mPrefs.edit().putString(USER_EMAIL, userDetail.getEmail()).apply();
        mPrefs.edit().putString(USER_GENDER, userDetail.getGender()).apply();
        mPrefs.edit().putString(USER_CONTACT, userDetail.getContactNo()).apply();
        mPrefs.edit().putString(USER_DIALING_CODE, userDetail.getDialingCode()).apply();
        mPrefs.edit().putString(USER_ADDRESS, userDetail.getAddress()).apply();
        mPrefs.edit().putString(USER_COUNTRY, userDetail.getCountry()).apply();
        mPrefs.edit().putString(GROUP_NAME, userDetail.getGroupName()).apply();
        mPrefs.edit().putInt(USER_MASTER_ID, userDetail.getUserMasterId()).apply();
        mPrefs.edit().putInt(USER_PANIC_ID, userDetail.getId()).apply();
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
    public boolean isIdentifyFeature() {
        return mPrefs.getBoolean(IDENTIFY_FEATURE, false);
    }

    @Override
    public void setIdentifyFeature(boolean notificationStatus) {
        mPrefs.edit().putBoolean(IDENTIFY_FEATURE, notificationStatus).apply();

    }

    @Override
    public String getLevelName() {
        return mPrefs.getString(LEVEL_NAME, "");
    }

    @Override
    public void setLevelName(String levelName) {
        mPrefs.edit().putString(LEVEL_NAME, levelName).apply();
    }

    @Override
    public boolean isCommercial() {
        return mPrefs.getBoolean(COMMERCIAL, false);
    }

    @Override
    public void setCommercial(boolean isCommercial) {
        mPrefs.edit().putBoolean(COMMERCIAL, isCommercial).apply();
    }

    @Override
    public String getPropertyCountry() {
        return mPrefs.getString(PROPERTY_COUNTRY, "");
    }

    @Override
    public void setPropertyCountry(String country) {
        mPrefs.edit().putString(PROPERTY_COUNTRY, country).apply();
    }

    @Override
    public String getPropertyDialingCode() {
        return mPrefs.getString(PROPERTY_DIALING_CODE, "254");
    }

    @Override
    public void setPropertyDialingCode(String dialingCode) {
        mPrefs.edit().putString(PROPERTY_DIALING_CODE, dialingCode).apply();
    }

    @Override
    public boolean isPrintLabel() {
        return mPrefs.getBoolean(PRINT_LABEL, false);
    }

    @Override
    public void setPrintLabel(boolean isPrint) {
        mPrefs.edit().putBoolean(PRINT_LABEL, isPrint).apply();
    }

    @Override
    public void setCaptureVehicleModel(boolean capture) {
        mPrefs.edit().putBoolean(CAPTURE_VEHICLE_MODEL,capture).apply();
    }

    @Override
    public boolean capturesVehicleModel() {
        return mPrefs.getBoolean(CAPTURE_VEHICLE_MODEL,false);
    }

    @Override
    public void setPrinterAddress(String address) {
        mPrefs.edit().putString(PRINTER_ADDRESS,address).apply();
    }

    @Override
    public String printerAddress() {
        return mPrefs.getString(PRINTER_ADDRESS,null);
    }

}
