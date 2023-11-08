package com.evisitor.data;

import android.app.Activity;
import android.content.Context;

import com.evisitor.BuildConfig;
import com.evisitor.data.local.prefs.AppPreferenceHelper;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.UserDetail;
import com.evisitor.data.remote.AppApiHelper;
import com.evisitor.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public class AppDataManager implements DataManager {

    private static AppDataManager instance;
    private final Gson mGson;
    private final AppApiHelper apiHelper;
    private final AppPreferenceHelper preferenceHelper;
    private Guests guests;
    private CommercialVisitorResponse.CommercialGuest commercialGuest;
    private ServiceProvider spDetail;
    private HouseKeepingResponse.ContentBean houseKeeping;
    private List<CountryResponse> countryResponseList;
    private GuestConfigurationResponse configurationResponse;
    private CommercialStaffResponse.ContentBean officeStaff;

    private AppDataManager(Context context) {
        apiHelper = AppApiHelper.getAppApiInstance();
        preferenceHelper = new AppPreferenceHelper(context);
        mGson = new GsonBuilder().create();
    }

    public static synchronized AppDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppDataManager(context);
        }
        return instance;
    }

    @Override
    public Guests getGuestDetail() {
        return guests;
    }

    @Override
    public void setGuestDetail(Guests guest) {
        this.guests = guest;
        this.spDetail = null;
        this.commercialGuest = null;
        this.houseKeeping = null;
        this.officeStaff = null;
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
        this.guests = null;
        this.commercialGuest = null;
        this.houseKeeping = null;
        this.officeStaff = null;
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
        this.spDetail = null;
        this.guests = null;
        this.commercialGuest = null;
        this.officeStaff = null;
    }

    @Override
    public String getImageBaseURL() {
        return BuildConfig.BASE_URL + "images/";
    }

    @Override
    public List<CountryResponse> getCountryResponseList() {
        return countryResponseList;
    }

    @Override
    public void setCountryResponse(List<CountryResponse> countryResponseList) {
        this.countryResponseList = countryResponseList;
    }

    @Override
    public GuestConfigurationResponse getGuestConfiguration() {
        return configurationResponse == null ? new GuestConfigurationResponse() : configurationResponse;
    }

    @Override
    public void setGuestConfiguration(GuestConfigurationResponse configurationResponse) {
        this.configurationResponse = configurationResponse;
    }

    @Override
    public CommercialVisitorResponse.CommercialGuest getCommercialVisitorDetail() {
        return commercialGuest;
    }

    @Override
    public void setCommercialVisitorDetail(CommercialVisitorResponse.CommercialGuest guest) {
        commercialGuest = guest;
        this.spDetail = null;
        this.guests = null;
        this.houseKeeping = null;
        this.officeStaff = null;
    }

    @Override
    public CommercialStaffResponse.ContentBean getCommercialStaff() {
        return officeStaff;
    }

    @Override
    public void setCommercialStaff(CommercialStaffResponse.ContentBean officeStaff) {
        this.officeStaff = officeStaff;
        this.spDetail = null;
        this.guests = null;
        this.commercialGuest = null;
        this.houseKeeping = null;
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
    public boolean isCheckOutFeature() {
        return preferenceHelper.isCheckOutFeature();
    }

    @Override
    public void setCheckOutFeature(boolean checkOutFeature) {
        preferenceHelper.setCheckOutFeature(checkOutFeature);
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
    public String getLevelName() {
        return preferenceHelper.getLevelName();
    }

    @Override
    public void setLevelName(String levelName) {
        preferenceHelper.setLevelName(levelName);
    }

    @Override
    public boolean isCommercial() {
        return preferenceHelper.isCommercial();
    }

    @Override
    public void setCommercial(boolean isCommercial) {
        preferenceHelper.setCommercial(isCommercial);
    }

    @Override
    public String getPropertyCountry() {
        return preferenceHelper.getPropertyCountry();
    }

    @Override
    public void setPropertyCountry(String country) {
        preferenceHelper.setPropertyCountry(country);
    }

    @Override
    public String getPropertyDialingCode() {
        return preferenceHelper.getPropertyDialingCode();
    }

    @Override
    public void setPropertyDialingCode(String dialingCode) {
        preferenceHelper.setPropertyDialingCode(dialingCode);
    }

    @Override
    public boolean isPrintLabel() {
        return preferenceHelper.isPrintLabel();
    }

    @Override
    public void setPrintLabel(boolean isPrint) {
        preferenceHelper.setPrintLabel(isPrint);
    }

    @Override
    public void setCaptureVehicleModel(boolean capture) {
        preferenceHelper.setCaptureVehicleModel(capture);
    }

    @Override
    public boolean capturesVehicleModel() {
        return preferenceHelper.capturesVehicleModel();
    }

    @Override
    public void setPrinterAddress(String address) {
        preferenceHelper.setPrinterAddress(address);
    }

    @Override
    public String printerAddress() {
        return preferenceHelper.printerAddress();
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
    public String getAccountName() {
        return preferenceHelper.getAccountName();
    }

    @Override
    public void setAccountName(String accountName) {
        preferenceHelper.setAccountName(accountName);
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
    public Call<ResponseBody> doGetGuestConfiguration(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetGuestConfiguration(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetConfigurations(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetConfigurations(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doAddGuest(String authToken, RequestBody requestBody) {
        return apiHelper.doAddGuest(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doAddSP(String authToken, RequestBody requestBody) {
        return apiHelper.doAddSP(authToken, requestBody);
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
    public Call<ResponseBody> doGetCommercialGuestCheckInOutList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialGuestCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetGuestCheckInOutList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetGuestCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHouseKeepingCheckInOutList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetHouseKeepingCheckInOutList(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetServiceProviderCheckInOutList(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetServiceProviderCheckInOutList(authToken, partMap);
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

    @Override
    public Call<ResponseBody> doCheckGuestStatus(String authToken, Map<String, String> partMap) {
        return apiHelper.doCheckGuestStatus(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetNotifications(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetNotifications(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doReadAllNotification(String authToken, Map<String, String> partMap) {
        return apiHelper.doReadAllNotification(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetHouseInfo(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetHouseInfo(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetPropertyInfo(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetPropertyInfo(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetRejectedVisitors(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetRejectedVisitors(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetProfileSuggestions(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetProfileSuggestions(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCompanySuggestions(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCompanySuggestions(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doAddCommercialGuest(String authToken, RequestBody requestBody) {
        return apiHelper.doAddCommercialGuest(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetExpectedCommercialGuestListDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetExpectedCommercialGuestListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaff(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialStaff(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffListDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialStaffListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCommercialStaffCheckInCheckOut(String authToken, RequestBody body) {
        return apiHelper.doCommercialStaffCheckInCheckOut(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffCheckInOutListDetail(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialStaffCheckInOutListDetail(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doCommercialSendNotification(String authToken, RequestBody body) {
        return apiHelper.doCommercialSendNotification(authToken, body);
    }

    @Override
    public Call<ResponseBody> doGetCommercialNotifications(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialNotifications(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetCommercialStaffByQRCode(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetCommercialStaffByQRCode(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetResidentByQRCode(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetResidentByQRCode(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doGetVisitorByQRCode(String authToken, RequestBody requestBody) {
        return apiHelper.doGetVisitorByQRCode(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetSpByQr(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetSpByQr(authToken, partMap);
    }


    @Override
    public Call<ResponseBody> doPasswordReset(RequestBody requestBody) {
        return apiHelper.doPasswordReset(requestBody);
    }

    @Override
    public Call<ResponseBody> doGetFilterVisitorInfo(String authToken, RequestBody requestBody) {
        return apiHelper.doGetFilterVisitorInfo(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doNumberPlateDetails(String authToken, MultipartBody.Part requestBody) {
        return apiHelper.doNumberPlateDetails(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doEnableDeviceNotification(String authToken, RequestBody requestBody) {
        return apiHelper.doEnableDeviceNotification(authToken, requestBody);
    }

    @Override
    public Call<Boolean> doGetDeviceNotificationStatus(String authToken, RequestBody requestBody) {
        return apiHelper.doGetDeviceNotificationStatus(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doPostPanicNotification(String authToken, RequestBody requestBody) {
        return apiHelper.doPostPanicNotification(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doResidentCheckInCheckOut(String authToken, RequestBody requestBody) {
        return apiHelper.doResidentCheckInCheckOut(authToken, requestBody);
    }

    @Override
    public Call<ResponseBody> doGetBase64ImageByName(String authToken, Map<String, String> partMap) {
        return apiHelper.doGetBase64ImageByName(authToken, partMap);
    }

    @Override
    public Call<ResponseBody> doPostDocument(RequestBody requestBody, MultipartBody.Part body) {
        return apiHelper.doPostDocument(requestBody, body);
    }
}
