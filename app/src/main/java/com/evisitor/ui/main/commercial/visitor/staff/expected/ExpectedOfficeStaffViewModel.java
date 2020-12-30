package com.evisitor.ui.main.commercial.visitor.staff.expected;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.OfficeStaffResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
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

public class ExpectedOfficeStaffViewModel extends BaseCheckInOutViewModel<ExpectedOfficeStaffNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    public ExpectedOfficeStaffViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getExpectedOfficeListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("type", "expected");
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            AppLogger.d("Searching : Expected", page + " : " + search);

            getDataManager().doGetCommercialOfficeListDetail(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            OfficeStaffResponse officeStaffResponse = getDataManager().getGson().fromJson(response.body().string(), OfficeStaffResponse.class);
                            if (officeStaffResponse.getContent() != null) {
                                getNavigator().onExpectedOFSuccess(officeStaffResponse.getContent());
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

    List<VisitorProfileBean> setClickVisitorDetail(OfficeStaffResponse.ContentBean hkBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setOfficeStaff(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, hkBean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, hkBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot, CalenderUtils.formatDate(hkBean.getCheckInTime(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM), CalenderUtils.formatDate(hkBean.getCheckOutTime(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), hkBean.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, hkBean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(hkBean.getDialingCode()).concat(" ").concat(hkBean.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, hkBean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getDocumentId())));
        if (!getDataManager().isCommercial() && !hkBean.getFlatNo().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), hkBean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getPremiseName())));
        } else if (hkBean.getCreatedBy() != null && !hkBean.getCreatedBy().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_register_by, hkBean.getCreatedBy())));
        if (hkBean.getCreatedDate() != null && !hkBean.getCreatedDate().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_register_date, CalenderUtils.formatDate(hkBean.getCreatedDate(), CalenderUtils.SERVER_DATE_FORMAT2, CalenderUtils.CUSTOM_TIMESTAMP_FORMAT_SLASH))));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void doCheckIn() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("staffId", getDataManager().getOfficeStaff().getStaffId());
                object.put("enteredVehicleNo", getDataManager().getOfficeStaff().getEnteredVehicleNo());
                object.put("type", AppConstants.CHECK_IN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getNavigator().showLoading();
            getDataManager().doOfficeStaffCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            JSONObject object1 = new JSONObject(response.body().string());
                            getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), object1.getString("result")).setOnPositiveClickListener(alertDialog -> {
                                alertDialog.dismiss();
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

    @Override
    public void onSuccess() {
        getNavigator().refreshList();
    }
}
