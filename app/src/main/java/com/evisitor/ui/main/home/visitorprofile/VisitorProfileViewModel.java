package com.evisitor.ui.main.home.visitorprofile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorProfileViewModel extends BaseViewModel<BaseNavigator> {

    private final MutableLiveData<String> houseNoInfo = new MutableLiveData<>();
    private final MutableLiveData<String> numberPlate = new MutableLiveData<>();
    private final MutableLiveData<String> bodyTemperature = new MutableLiveData<>();

    public MutableLiveData<String> getBodyTemperature() {
        return bodyTemperature;
    }

    public VisitorProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<String> getHouseNoInfo() {
        return houseNoInfo;
    }

    public MutableLiveData<String> getNumberPlate() {
        return numberPlate;
    }

    void getHouseInfo(String flatId) {
        if (flatId.isEmpty()) return;
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("flatId", flatId);

            getDataManager().doGetHouseInfo(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("result")) {
                                houseNoInfo.setValue(jsonObject.getString("result"));
                            }
                        }
                    } catch (Exception e) {
                        AppLogger.w("VisitorProfileViewModel", e.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                }
            });
        }
    }

    void numberPlateVerification(Bitmap bitmap){
        MultipartBody.Part body = AppUtils.prepareFilePart("upload","image/png",AppUtils.bitmapToFile(getNavigator().getContext(),bitmap,UUID.randomUUID().toString().concat(".png")));
        getDataManager().doNumberPlateDetails(AppConstants.TOKEN.concat(" ").concat(getDataManager().getUserDetail().getApiKey()),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                try {
                    if (response.code() == 201) {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.has("results")){
                            JSONArray array = jsonObject.getJSONArray("results");
                            if(array.length()>0){
                                if(array.getJSONObject(0).has("plate")){
                                    String numerPlate = array.getJSONObject(0).getString("plate");
                                    numberPlate.setValue(numerPlate);
                                }
                            }
                        }

                    } else if (response.code() == 401) {
                        getNavigator().openActivityOnTokenExpire();
                    } else getNavigator().handleApiError(response.errorBody());
                } catch (Exception e) {
                    AppLogger.w("VisitorProfileViewModel", e.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }


    void doFindBodyTemperature(){
        if (getDataManager().isCommercial()) {
            CommercialVisitorResponse.CommercialGuest guests = getDataManager().getCommercialVisitorDetail();
            if (guests != null) {
                bodyTemperature.setValue(guests.getBodyTemperature());
            }

            CommercialStaffResponse.ContentBean staff = getDataManager().getCommercialStaff();
            if (staff != null) {
                bodyTemperature.setValue(staff.getBodyTemperature());
            }

        } else {
            Guests guests = getDataManager().getGuestDetail();
            if (guests != null) {
                bodyTemperature.setValue(guests.getBodyTemperature());
            }

            HouseKeepingResponse.ContentBean hkBean = getDataManager().getHouseKeeping();
            if (hkBean != null) {
                bodyTemperature.setValue(hkBean.getBodyTemperature());            }
        }

        ServiceProvider spBean = getDataManager().getSpDetail();
        if (spBean != null) {
            bodyTemperature.setValue(spBean.getBodyTemperature());
        }
    }
}
