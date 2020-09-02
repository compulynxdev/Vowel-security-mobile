package com.evisitor.ui.main.home.housekeeping.expected;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.HouseKeepingCheckInResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppUtils;

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

public class ExpectedHKViewModel extends BaseViewModel<ExpectedHKNavigator> {
    private MutableLiveData<List<HouseKeeping>> hkListData = new MutableLiveData<>();

    public ExpectedHKViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HouseKeeping>> getHkListData() {
        return hkListData;
    }

    void getExpectedHKListData(int page, String search) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));

        getDataManager().doGetExpectedSPList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        HouseKeepingCheckInResponse houseKeepingResponse = getDataManager().getGson().fromJson(response.body().string(), HouseKeepingCheckInResponse.class);
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

    List<VisitorProfileBean> setClickVisitorDetail(HouseKeeping hkBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setHouseKeeping(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, hkBean.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, hkBean.getProfile())));
        //visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), spBean.getExpectedVehicleNo(), true));
        if (!hkBean.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, hkBean.getContactNo())));
        if (!hkBean.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, hkBean.getIdentityNo())));
        if (hkBean.getHouseNo().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getCreatedBy())));
        } else {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, hkBean.getHouseNo())));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getHost())));
        }
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getDataManager().getHouseKeeping().getHouseKeeperId());
            object.put("accountId", getDataManager().getAccountId());
            object.put("residentId", getDataManager().getHouseKeeping().getResidentId());
            object.put("premiseHierarchyDetailsId", getDataManager().getHouseKeeping().getFlatId());
            object.put("type", AppConstants.HOUSE_HELP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getNavigator().showLoading();
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doGuestSendNotification(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess)
                            , getNavigator().getContext().getString(R.string.send_notification_success)).setOnPositiveClickListener(dialog -> {
                        dialog.dismiss();
                        getNavigator().refreshList();
                    });
                } else if (response.code() == 401) {
                    getNavigator().openActivityOnTokenExpire();
                } else {
                    getNavigator().handleApiError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });

    }

    void approveByCall() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getDataManager().getHouseKeeping().getHouseKeeperId());
            object.put("enteredVehicleNo", getDataManager().getHouseKeeping().getEnteredVehicleNo());
            object.put("type", AppConstants.CHECK_IN);
            object.put("visitor", AppConstants.HOUSE_HELP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getNavigator().showLoading();
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object1 = new JSONObject(response.body().string());
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess), object1.getString("result")).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();
                            getNavigator().refreshList();
                        });
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                } else if (response.code() == 401) {
                    getNavigator().openActivityOnTokenExpire();
                } else {
                    getNavigator().handleApiError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }
}
