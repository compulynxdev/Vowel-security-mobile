package com.evisitor.data;

import android.app.Activity;
import android.content.Context;

import com.evisitor.data.local.prefs.AppPreferenceHelper;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.UserDetail;
import com.evisitor.data.remote.AppApiHelper;
import com.evisitor.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppDataManager implements DataManager {

    private AppApiHelper apiHelper;
    private AppPreferenceHelper preferenceHelper;
    private static AppDataManager instance;
    private final Gson mGson;

    private AppDataManager(Context context) {
        apiHelper = AppApiHelper.getAppApiInstance();
        preferenceHelper = new AppPreferenceHelper(context);
        mGson = new GsonBuilder().create();
    }

    public static synchronized AppDataManager getInstance(Context context){
        if (instance==null){
            instance = new AppDataManager(context);
        }
        return instance;
    }


    @Override
    public Gson getGson() {
        return mGson;
    }

    @Override
    public String getHeader() {
        return AppConstants.bearer.concat(" ").concat(preferenceHelper.getAccessToken());
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
    public boolean isRememberMe() {
        return preferenceHelper.isRememberMe();
    }

    @Override
    public void setRememberMe(boolean isRemember) {
        preferenceHelper.setRememberMe(isRemember);
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
    public String getAccountId() {
        return preferenceHelper.getAccountId();
    }

    @Override
    public void setAccountId(String accId) {
        preferenceHelper.setAccountId(accId);
    }

    @Override
    public String getUserId() {
        return preferenceHelper.getUserId();
    }

    @Override
    public void setUserId(String userId) {
        preferenceHelper.setUserId(userId);
    }

    @Override
    public UserDetail getUserDetail() {
        return preferenceHelper.getUserDetail();
    }

    @Override
    public void setUserDetail(UserDetail userDetail) {
        preferenceHelper.setUserDetail(userDetail);
    }

    @Override
    public Call<ResponseBody> doLogin(RequestBody requestBody) {
        return apiHelper.doLogin(requestBody);
    }

    @Override
    public Call<ResponseBody> doGetUserDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetUserDetail(authToken, partMap);
    }

    @Override
    public Call<GuestsResponse> doGetExpectedGuestListDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetExpectedGuestListDetail(authToken, partMap);
    }
}
