package com.evisitor.data.remote;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface ApiHelper {
    @POST(WebServices.LOGIN_AUTH)
    Call<ResponseBody> doLogin(@Body RequestBody requestBody);

    @GET(WebServices.GET_USER_DETAIL)
    Call<ResponseBody> doGetUserDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_GUEST_CONFIGURATION)
    Call<ResponseBody> doGetGuestConfiguration(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_CONFIGURATIONS)
    Call<ResponseBody> doGetConfigurations(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.ADD_GUEST)
    Call<ResponseBody> doAddGuest(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @POST(WebServices.ADD_SP)
    Call<ResponseBody> doAddSP(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @GET(WebServices.GET_HOUSE_LIST)
    Call<ResponseBody> doGetHouseDetailList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_HOST_LIST)
    Call<ResponseBody> doGetHostDetailList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_EXPECTED_GUEST_LIST)
    Call<ResponseBody> doGetExpectedGuestListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.CHECKIN_CHECKOUT)
    Call<ResponseBody> doCheckInCheckOut(@Header("authorization") String authToken, @Body RequestBody body);

    @POST(WebServices.GUEST_SEND_NOTIFICATION)
    Call<ResponseBody> doGuestSendNotification(@Header("authorization") String authToken, @Body RequestBody body);

    @GET(WebServices.GET_COMMERCIAL_GUEST_CHECKIN_CHECKOUT_LIST)
    Call<ResponseBody> doGetCommercialGuestCheckInOutList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_GUEST_CHECKIN_CHECKOUT_LIST)
    Call<ResponseBody> doGetGuestCheckInOutList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_HOUSEKEEPING_CHECKIN_CHECKOUT_LIST)
    Call<ResponseBody> doGetHouseKeepingCheckInOutList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_SERVICE_PROVIDER_CHECKIN_CHECKOUT_LIST)
    Call<ResponseBody> doGetServiceProviderCheckInOutList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_EXPECTED_SP_LIST)
    Call<ResponseBody> doGetExpectedSPList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_REGISTERED_HOUSE_KEEPING_LIST)
    Call<ResponseBody> doGetRegisteredHKList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_BLACK_LIST_VISITORS)
    Call<ResponseBody> doGetBlackListVisitors(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_ALL_TRESPASSER_GUEST)
    Call<ResponseBody> doGetAllTrespasserGuest(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_ALL_TRESPSSER_SP)
    Call<ResponseBody> doGetAllTrespasserSP(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_ALL_FLAG_VISITORS)
    Call<ResponseBody> doGetALLFlagVisitors(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_VISITOR_COUNT)
    Call<ResponseBody> doGetVisitorCount(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.CHECK_GUEST_STATUS)
    Call<ResponseBody> doCheckGuestStatus(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_NOTIFICATIONS)
    Call<ResponseBody> doGetNotifications(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.READ_ALL_NOTIFICATIONS)
    Call<ResponseBody> doReadAllNotification(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_HOUSE_INFO)
    Call<ResponseBody> doGetHouseInfo(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_PROPERTY_INFO)
    Call<ResponseBody> doGetPropertyInfo(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_REJECT_VISITORS)
    Call<ResponseBody> doGetRejectedVisitors(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_PROFILES_SUGGESTIONS)
    Call<ResponseBody> doGetProfileSuggestions(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_COMPANY_SUGGESTIONS)
    Call<ResponseBody> doGetCompanySuggestions(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.ADD_COMMERCIAL_GUEST)
    Call<ResponseBody> doAddCommercialGuest(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @GET(WebServices.GET_EXPECTED_COMMERCIAL_GUEST_LIST)
    Call<ResponseBody> doGetExpectedCommercialGuestListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_COMMERCIAL_STAFF)
    Call<ResponseBody> doGetCommercialStaff(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_ALL_REGISTERED_COMMERCIAL_STAFF)
    Call<ResponseBody> doGetCommercialStaffListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.COMMERCIAL_STAFF_CHECK_IN_OUT)
    Call<ResponseBody> doCommercialStaffCheckInCheckOut(@Header("authorization") String authToken, @Body RequestBody body);

    @GET(WebServices.GET_COMMERCIAL_CHECKIN_CHECKOUT_STAFF)
    Call<ResponseBody> doGetCommercialStaffCheckInOutListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.COMMERCIAL_SEND_NOTIFICATION)
    Call<ResponseBody> doCommercialSendNotification(@Header("authorization") String authToken, @Body RequestBody body);

    @GET(WebServices.GET_COMMERCIAL_NOTIFICATION)
    Call<ResponseBody> doGetCommercialNotifications(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_STAFF_BY_QR_CODE)
    Call<ResponseBody> doGetCommercialStaffByQRCode(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_RESIDENT_BY_QR_CODE)
    Call<ResponseBody> doGetResidentByQRCode(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.GET_VISITOR_BY_QR_CODE)
    Call<ResponseBody> doGetVisitorByQRCode(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @GET(WebServices.GET_SP_BY_QRCODE)
    Call<ResponseBody> doGetSpByQr(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.POST_TEST_PASSWORD_REQUEST)
    Call<ResponseBody> doPasswordReset(@Body RequestBody requestBody);

    @POST(WebServices.GET_FILTER_VISITOR_INFO)
    Call<ResponseBody> doGetFilterVisitorInfo(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @Multipart
    @POST(WebServices.PLATE_READER)
    Call<ResponseBody> doNumberPlateDetails(@Header("Authorization") String authToken, @Part MultipartBody.Part file);

    @POST(WebServices.ENABLE_DEVICE_NOTIFICATION)
    Call<ResponseBody> doEnableDeviceNotification(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @POST(WebServices.DEVICE_NOTIFICATION_STATUS)
    Call<Boolean> doGetDeviceNotificationStatus(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @POST(WebServices.SEND_PANIC_NOTIFICATION)
    Call<ResponseBody> doPostPanicNotification(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @POST(WebServices.RESIDENT_CHECK_IN_OUT)
    Call<ResponseBody> doResidentCheckInCheckOut(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @GET(WebServices.GET_IMAGE_BY_NAME)
    Call<ResponseBody> doGetBase64ImageByName(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @Multipart
    @POST(WebServices.MRZ_DATA_EXTRACTION)
    Call<ResponseBody> doPostDocument(@Part("imagefile") RequestBody requestBody, @Part MultipartBody.Part body);
}
