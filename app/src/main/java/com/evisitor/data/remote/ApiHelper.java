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
}
