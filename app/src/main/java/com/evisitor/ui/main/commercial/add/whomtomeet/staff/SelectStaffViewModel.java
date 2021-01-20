package com.evisitor.ui.main.commercial.add.whomtomeet.staff;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.SelectCommercialStaffResponse;
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


public class SelectStaffViewModel extends BaseViewModel<SelectStaffNavigator> {

    private List<SelectCommercialStaffResponse> commercialStaffLists = new ArrayList<>();

    public SelectStaffViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getStaffListData() {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());

            getDataManager().doGetCommercialStaff(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<SelectCommercialStaffResponse>>() {
                            }.getType();
                            List<SelectCommercialStaffResponse> commercialStaffList = getDataManager().getGson().fromJson(response.body().string(), listType);
                            if (commercialStaffList != null) {
                                commercialStaffLists = commercialStaffList;
                                getNavigator().onStaffDataReceived(commercialStaffList);
                            }
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else getNavigator().handleApiError(response.errorBody());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
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
        List<SelectCommercialStaffResponse> commercialStaff = new ArrayList<>();

        for (SelectCommercialStaffResponse bean : commercialStaffLists) {
            if (bean.getFullName().toLowerCase().contains(search.toLowerCase()))
                commercialStaff.add(bean);
        }
        getNavigator().onStaffDataReceived(commercialStaff);
    }
}