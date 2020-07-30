package com.evisitor.data.remote;

import com.evisitor.data.model.GuestsResponse;

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

    @GET(WebServices.GET_EXPECTED_GUEST_LIST_DETAIL)
    Call<GuestsResponse> doGetExpectedGuestListDetail(@Header("authorization") String authToken, @QueryMap Map<String, String> partMap);

    @POST(WebServices.GUEST_CHECKIN_CHECKOUT)
    Call<ResponseBody> doGuestCheckInCheckOut(@Header("authorization") String authToken, @Body RequestBody body);

    @POST(WebServices.GUEST_SEND_NOTIFICTION)
    Call<ResponseBody> doGuestSendNotification(@Header("authorization") String authToken, @Body RequestBody body);

}
