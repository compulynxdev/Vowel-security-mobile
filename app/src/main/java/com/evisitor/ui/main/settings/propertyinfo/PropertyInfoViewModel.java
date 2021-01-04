package com.evisitor.ui.main.settings.propertyinfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Hemant Sharma on 15-01-20.
 * Divergent software labs pvt. ltd
 */
public class PropertyInfoViewModel extends BaseViewModel<BaseNavigator> {

    private final MutableLiveData<PropertyInfoResponse> propertyInfoResponseMutableData = new MutableLiveData<>();

    public PropertyInfoViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<PropertyInfoResponse> getPropertyInfo() {
        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());

            getDataManager().doGetPropertyInfo(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            PropertyInfoResponse propertyInfoResponse = getDataManager().getGson().fromJson(response.body().string(), PropertyInfoResponse.class);
                            propertyInfoResponseMutableData.setValue(propertyInfoResponse);
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else getNavigator().handleApiError(response.errorBody());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        }
        return propertyInfoResponseMutableData;
    }

}
