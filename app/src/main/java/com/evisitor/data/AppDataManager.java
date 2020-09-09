package com.evisitor.data;

import android.app.Activity;
import android.content.Context;

import com.evisitor.BuildConfig;
import com.evisitor.data.local.prefs.AppPreferenceHelper;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
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
    private Guests guests;
    private ServiceProvider spDetail;
    private HouseKeepingResponse.ContentBean houseKeeping;

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
    public void setGuestDetail(Guests guest) {
        this.guests = guest;
    }

    @Override
    public Guests getGuestDetail() {
        return guests;
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
    public void setSPDetail(ServiceProvider spDetail) {
        this.spDetail = spDetail;
    }

    @Override
    public ServiceProvider getSpDetail() {
        return spDetail;
    }

    @Override
    public HouseKeepingResponse.ContentBean getHouseKeeping() {
        return houseKeeping;
    }

    @Override
    public void setHouseKeeping(HouseKeepingResponse.ContentBean houseKeeping) {
        this.houseKeeping = houseKeeping;
    }

    @Override
    public String getImageBaseURL() {
        return BuildConfig.BASE_URL + "images/";
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
    public boolean isIdentifyFeature() {
        return preferenceHelper.isIdentifyFeature();
    }

    @Override
    public void setIdentifyFeature(boolean notificationStatus) {
        preferenceHelper.setIdentifyFeature(notificationStatus);
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
    public Call<ResponseBody> doAddGuest(String authToken, RequestBody requestBody) {
        return apiHelper.doAddGuest(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetHouseDetailList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetHouseDetailList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHostDetailList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetHostDetailList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetExpectedGuestListDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetExpectedGuestListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCheckInCheckOut(String authToken, RequestBody body) {
        return apiHelper.doCheckInCheckOut(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGuestSendNotification(String authToken, RequestBody body) {
        return apiHelper.doGuestSendNotification(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetGuestCheckInList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetGuestCheckInList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHouseKeepingCheckInList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetHouseKeepingCheckInList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetServiceProviderCheckInList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetServiceProviderCheckInList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetExpectedSPList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetExpectedSPList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetRegisteredHKList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetRegisteredHKList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetBlackListVisitors(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetBlackListVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetAllTrespasserGuest(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetAllTrespasserGuest(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetAllTrespasserSP(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetAllTrespasserSP(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetALLFlagVisitors(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetALLFlagVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetVisitorCount(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetVisitorCount(authToken, partMap);
    }
}
