package com.evisitor.ui.main.home.housekeeping.expected;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.RegisteredHKResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedHKViewModel extends BaseCheckInOutViewModel<ExpectedHKNavigator> implements BaseCheckInOutViewModel.ApiCallback {
    private MutableLiveData<List<RegisteredHKResponse.ContentBean>> hkListData = new MutableLiveData<>();

    public ExpectedHKViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<RegisteredHKResponse.ContentBean>> getHkListData() {
        return hkListData;
    }

    void getExpectedHKListData(int page, String search) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        map.put("type", "expected");
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));

        getDataManager().doGetRegisteredHKList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        RegisteredHKResponse houseKeepingResponse = getDataManager().getGson().fromJson(response.body().string(), RegisteredHKResponse.class);
                        if (houseKeepingResponse.getContent() != null) {
                            hkListData.setValue(houseKeepingResponse.getContent());
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
                getNavigator().hideSwipeToRefresh();
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }

    List<VisitorProfileBean> setClickVisitorDetail(RegisteredHKResponse.ContentBean hkBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setHouseKeeping(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, hkBean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, hkBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot, CalenderUtils.formatDate(hkBean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM), CalenderUtils.formatDate(hkBean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), hkBean.getExpectedVehicleNo(), true));
        if (!hkBean.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, hkBean.getContactNo())));
        if (!hkBean.getDocumentId().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, hkBean.getDocumentId())));
        if (hkBean.getFlatNo().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getCreatedBy())));
        } else {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, hkBean.getFlatNo())));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getResidentName())));
        }
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", String.valueOf(getDataManager().getHouseKeeping().getId()));
            object.put("accountId", getDataManager().getAccountId());
            object.put("residentId", getDataManager().getHouseKeeping().getResidentId());
            object.put("premiseHierarchyDetailsId", getDataManager().getHouseKeeping().getFlatId());
            object.put("type", AppConstants.HOUSE_HELP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        sendNotification(body, this);
    }

    void approveByCall() {
        JSONObject object = new JSONObject();
        try {
            object.put("accountId", getDataManager().getAccountId());
            object.put("staffId", String.valueOf(getDataManager().getHouseKeeping().getId()));
            object.put("enteredVehicleNo", getDataManager().getHouseKeeping().getEnteredVehicleNo());
            object.put("type", AppConstants.CHECK_IN);
            object.put("visitor", AppConstants.HOUSE_HELP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        doCheckInOut(body, this);
    }

    @Override
    public void onSuccess() {
        getNavigator().refreshList();
    }
}
