package com.evisitor.ui.main.commercial.add.whomtomeet.level;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.ui.base.BaseViewModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectLastLevelViewModel extends BaseViewModel<SelectLastLevelNavigator> {
    private List<HouseDetailBean> houseDetailBeanList = new ArrayList<>();

    public SelectLastLevelViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void doGetLastLevelData() {
        if (getNavigator().isNetworkConnected(false)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("search", "");
            getDataManager().doGetHouseDetailList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<HouseDetailBean>>() {
                            }.getType();
                            List<HouseDetailBean> houseDetailList = getDataManager().getGson().fromJson(response.body().string(), listType);
                            if (houseDetailList != null) {
                                houseDetailBeanList = houseDetailList;
                                getNavigator().onLastLevelDataReceived(houseDetailList);
                            }
                        } catch (Exception e) {
                            getNavigator().showToast(R.string.alert_error);
                        }
                    } else if (response.code() == 401) {
                        getNavigator().openActivityOnTokenExpire();
                    }  else {
                        getNavigator().handleApiError(response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().handleApiFailure(t);
                }
            });
        }
    }

    void getSearchData(String search) {
        List<HouseDetailBean> houseDetailBeans = new ArrayList<>();
        for (HouseDetailBean bean : houseDetailBeanList) {
            if (bean.getName().toLowerCase().contains(search.toLowerCase()))
                houseDetailBeans.add(bean);
        }
        getNavigator().onLastLevelDataReceived(houseDetailBeans);
    }
}