package com.evisitor.ui.main.residential.staff.expected;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;

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

    private static final String TAG = "ExpectedHKViewModel";

    public ExpectedHKViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getExpectedHKListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("type", "expected");
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            AppLogger.d("Searching : ExpectedHK", page + " : " + search);

            getDataManager().doGetRegisteredHKList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            HouseKeepingResponse houseKeepingResponse = getDataManager().getGson().fromJson(response.body().string(), HouseKeepingResponse.class);
                            if (houseKeepingResponse.getContent() != null) {
                                getNavigator().onExpectedHKSuccess(houseKeepingResponse.getContent());
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
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(HouseKeepingResponse.ContentBean hkBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setHouseKeeping(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, hkBean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, hkBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot, CalenderUtils.formatDateWithOutUTC(hkBean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM), CalenderUtils.formatDateWithOutUTC(hkBean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), hkBean.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, hkBean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(hkBean.getDialingCode()).concat(" ").concat(hkBean.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, hkBean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(hkBean.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, hkBean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(hkBean.getDocumentId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, hkBean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getNationality())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, hkBean.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getCompanyName())));

        if (!getDataManager().isCommercial()) {
            if (hkBean.getFlatNo().isEmpty()) {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getCreatedBy())));
            } else {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), hkBean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getPremiseName())));
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getResidentName())));
            }
        }
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, hkBean.getMode())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", String.valueOf(getDataManager().getHouseKeeping().getId()));
                object.put("accountId", getDataManager().getAccountId());
                object.put("residentId", getDataManager().getHouseKeeping().getResidentId());
                object.put("premiseHierarchyDetailsId", getDataManager().getHouseKeeping().getFlatId());
                object.put("enteredVehicleNo", getDataManager().getHouseKeeping().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getHouseKeeping().getBodyTemperature());
                object.put("type", AppConstants.HOUSE_HELP);
            } catch (JSONException e) {
                AppLogger.w(TAG, e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            sendNotification(body, this);
        }
    }

    void approveByCall(boolean isAccept, String input) {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("staffId", String.valueOf(getDataManager().getHouseKeeping().getId()));
                object.put("enteredVehicleNo", getDataManager().getHouseKeeping().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getHouseKeeping().getBodyTemperature());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.HOUSE_HELP);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy", isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("rejectReason", input);

                if (getDataManager().getHouseKeeping().getVehicalBitmapImg() != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getHouseKeeping().getVehicalBitmapImg()));
                }

            } catch (JSONException e) {
                AppLogger.w(TAG, e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            doCheckInOut(body, this);
        }
    }

    @Override
    public void onSuccess() {
        getNavigator().refreshList();
    }
}
