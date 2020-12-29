package com.evisitor.ui.main.home.visitorprofile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorProfileViewModel extends BaseViewModel<BaseNavigator> {

    private MutableLiveData<String> houseNoInfo = new MutableLiveData<>();

    public VisitorProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<String> getHouseNoInfo() {
        return houseNoInfo;
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
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                }
            });
        }
    }
}
