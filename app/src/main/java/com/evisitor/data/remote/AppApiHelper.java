package com.evisitor.data.remote;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppApiHelper implements ApiHelper {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    private static ApiHelper apiInterface = null;
    private static AppApiHelper apiHelper;

    private AppApiHelper() {

    }

    public synchronized static AppApiHelper getAppApiInstance() {
        if (apiHelper == null) {
            apiHelper = new AppApiHelper();
        }
        return apiHelper;
    }

    private static ApiHelper getApiInterface() {
        apiInterface = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WebServices.BASE_URL)
                .client(okHttpClient)
                .build().create(ApiHelper.class);

        return apiInterface;
    }

    private static ApiHelper getNumberPlateUrl() {
        apiInterface = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WebServices.BASE_PLATE_RECOGNISER)
                .client(okHttpClient)
                .build().create(ApiHelper.class);
        return apiInterface;
    }

    @Override
    public Call<ResponseBody> doLogin(RequestBody requestBody) {
        return getApiInterface().doLogin(requestBody);
    }

    @Override
    public Call<ResponseBody> doGetUserDetail(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetUserDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetGuestConfiguration(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetGuestConfiguration(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetConfigurations(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetConfigurations(authToken,partMap);
    }

    @Override
    public Call<ResponseBody> doAddGuest(String authToken, RequestBody requestBody) {
        return getApiInterface().doAddGuest(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doAddSP(String authToken, RequestBody requestBody) {
        return getApiInterface().doAddSP(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetHouseDetailList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetHouseDetailList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHostDetailList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetHostDetailList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetExpectedGuestListDetail(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetExpectedGuestListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCheckInCheckOut(String authToken, RequestBody body) {
        return getApiInterface().doCheckInCheckOut(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGuestSendNotification(String authToken, RequestBody body) {
        return getApiInterface().doGuestSendNotification(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetCommercialGuestCheckInOutList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialGuestCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetGuestCheckInOutList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetGuestCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHouseKeepingCheckInOutList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetHouseKeepingCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetServiceProviderCheckInOutList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetServiceProviderCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetExpectedSPList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetExpectedSPList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetRegisteredHKList(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetRegisteredHKList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetBlackListVisitors(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetBlackListVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetAllTrespasserGuest(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetAllTrespasserGuest(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetAllTrespasserSP(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetAllTrespasserSP(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetALLFlagVisitors(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetALLFlagVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetVisitorCount(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetVisitorCount(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCheckGuestStatus(String authToken, Map<String, String> partMap) {
        return getApiInterface().doCheckGuestStatus(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetNotifications(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetNotifications(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doReadAllNotification(String authToken, Map<String, String> partMap) {
        return getApiInterface().doReadAllNotification(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHouseInfo(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetHouseInfo(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetPropertyInfo(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetPropertyInfo(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetRejectedVisitors(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetRejectedVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetProfileSuggestions(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetProfileSuggestions(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCompanySuggestions(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCompanySuggestions(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doAddCommercialGuest(String authToken, RequestBody requestBody) {
        return getApiInterface().doAddCommercialGuest(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetExpectedCommercialGuestListDetail(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetExpectedCommercialGuestListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaff(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialStaff(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffListDetail(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialStaffListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCommercialStaffCheckInCheckOut(String authToken, RequestBody body) {
        return getApiInterface().doCommercialStaffCheckInCheckOut(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffCheckInOutListDetail(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialStaffCheckInOutListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCommercialSendNotification(String authToken, RequestBody body) {
        return getApiInterface().doCommercialSendNotification(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetCommercialNotifications(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialNotifications(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffByQRCode(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetCommercialStaffByQRCode(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetResidentByQRCode(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetResidentByQRCode(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetVisitorByQRCode(String authToken, RequestBody requestBody) {
        return getApiInterface().doGetVisitorByQRCode(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetSpByQr(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetSpByQr(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doPasswordReset(RequestBody requestBody) {
        return getApiInterface().doPasswordReset(requestBody);
    }

    @Override
    public Call<ResponseBody> doGetFilterVisitorInfo(String authToken, RequestBody requestBody) {
        return getApiInterface().doGetFilterVisitorInfo(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doNumberPlateDetails(String authToken, MultipartBody.Part file) {
        return getNumberPlateUrl().doNumberPlateDetails(authToken, file);
    }

    @Override
    public Call<ResponseBody> doEnableDeviceNotification(String authToken, RequestBody requestBody) {
        return getApiInterface().doEnableDeviceNotification(authToken, requestBody);
    }

    @Override
    public Call<Boolean> doGetDeviceNotificationStatus(String authToken, RequestBody requestBody) {
        return getApiInterface().doGetDeviceNotificationStatus(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doPostPanicNotification(String authToken, RequestBody requestBody) {
        return getApiInterface().doPostPanicNotification(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doResidentCheckInCheckOut(String authToken, RequestBody requestBody) {
        return getApiInterface().doResidentCheckInCheckOut(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetBase64ImageByName(String authToken, Map<String, String> partMap) {
        return getApiInterface().doGetBase64ImageByName(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doPostDocument(RequestBody requestBody, MultipartBody.Part body) {
        return getApiInterface().doPostDocument(requestBody, body);
    }
}
