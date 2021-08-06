package com.evisitor.ui.main.home.rejected.staff;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RejectedStaffViewModel extends BaseViewModel<RejectedStaffNavigator> {
    public RejectedStaffViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getDomesticStaff(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", "staff");
            AppLogger.d("Searching : Trespasser", page + " : " + search);

            getDataManager().doGetRejectedVisitors(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            HouseKeepingResponse hkResponse = getDataManager().getGson().fromJson(response.body().string(), HouseKeepingResponse.class);
                            if (hkResponse != null) {
                                AppLogger.d("Searching : Size", "" + hkResponse.getContent().size());
                                getNavigator().onSuccess(hkResponse.getContent());
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

    List<VisitorProfileBean> getVisitorDetail(HouseKeepingResponse.ContentBean hkBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, hkBean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, hkBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, hkBean.getExpectedVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getExpectedVehicleNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, hkBean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(hkBean.getDialingCode()).concat(" ").concat(hkBean.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, hkBean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(hkBean.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, hkBean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(hkBean.getDocumentId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, hkBean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getNationality())));

        if (!getDataManager().isCommercial()) {
            if (hkBean.getPremiseName().isEmpty()) {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getCreatedBy())));
            } else {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), hkBean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getPremiseName())));
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, hkBean.getResidentName())));
            }
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.rejected_by, hkBean.getRejectedBy().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getRejectedBy())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.rejected_on, hkBean.getRejectedOn().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDate(hkBean.getRejectedOn(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_reason, hkBean.getRejectReason().isEmpty() ? getNavigator().getContext().getString(R.string.na) : hkBean.getRejectReason())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

}
