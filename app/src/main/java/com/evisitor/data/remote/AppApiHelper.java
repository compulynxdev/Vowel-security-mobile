package com.evisitor.data.remote;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private static ApiHelper apiInterface = null;
    private static AppApiHelper apiHelper;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    public synchronized static AppApiHelper getAppApiInstance() {
        if (apiHelper == null) {
            apiHelper = new AppApiHelper();
        }
        return apiHelper;
    }

    private AppApiHelper() {

    }

    private static ApiHelper getApiInterface() {
        if (apiInterface == null) {
            apiInterface = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(WebServices.BASE_URL)
                    .client(okHttpClient)
                    .build().create(ApiHelper.class);
        }
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
}
