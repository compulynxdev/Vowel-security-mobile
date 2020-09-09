package com.evisitor.data.remote;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface ApiHelper {
    @POST(WebServices.LOGIN_AUTH)
    Call<ResponseBody> doLogin(@Body RequestBody requestBody);

    @GET(WebServices.GET_USER_DETAIL)
    Call<ResponseBody> doGetUserDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.ADD_GUEST)
    Call<ResponseBody> doAddGuest(@Header("authorization") String authToken, @Body RequestBody requestBody);

    @GET(WebServices.GET_HOUSE_LIST)
    Call<ResponseBody> doGetHouseDetailList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_HOST_LIST)
    Call<ResponseBody> doGetHostDetailList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_EXPECTED_GUEST_LIST)
    Call<ResponseBody> doGetExpectedGuestListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.CHECKIN_CHECKOUT)
    Call<ResponseBody> doCheckInCheckOut(@Header("authorization") String authToken, @Body RequestBody body);

    @POST(WebServices.GUEST_SEND_NOTIFICTION)
    Call<ResponseBody> doGuestSendNotification(@Header("authorization") String authToken, @Body RequestBody body);

    @GET(WebServices.GET_GUEST_CHECKIN_LIST)
    Call<ResponseBody> doGetGuestCheckInList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_HOUSEKEEPING_CHECKIN_LIST)
    Call<ResponseBody> doGetHouseKeepingCheckInList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @GET(WebServices.GET_SERVICE_PROVIDER_CHECKIN_LIST)
    Call<ResponseBody> doGetServiceProviderCheckInList(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

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
}
