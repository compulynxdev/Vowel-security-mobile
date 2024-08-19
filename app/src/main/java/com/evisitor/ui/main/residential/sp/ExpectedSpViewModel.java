package com.evisitor.ui.main.residential.sp;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.ServiceProviderResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedSpViewModel extends BaseCheckInOutViewModel<ExpectedSPNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    private static final String TAG = "ExpectedSpViewModel";

    public ExpectedSpViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getSpListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            AppLogger.d("Searching : ExpectedSP", page + " : " + search);

            getDataManager().doGetExpectedSPList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            ServiceProviderResponse spResponse = getDataManager().getGson().fromJson(response.body().string(), ServiceProviderResponse.class);
                            if (spResponse.getContent() != null) {
                                getNavigator().onExpectedSPSuccess(spResponse.getContent());
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                    getNavigator().hideSwipeToRefresh();
                }
            });
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }

    }

    List<VisitorProfileBean> setClickVisitorDetail(ServiceProvider spBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setSPDetail(spBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, spBean.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, spBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), spBean.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, spBean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(spBean.getDialingCode()).concat(" ").concat(spBean.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, spBean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(spBean.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, spBean.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(spBean.getIdentityNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, spBean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : spBean.getNationality())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, spBean.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : spBean.getCompanyName())));

        if (!getDataManager().isCommercial()) {
            if (spBean.getHouseNo().isEmpty()) {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, spBean.getCreatedBy())));
            } else {
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), spBean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : spBean.getPremiseName())));
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, spBean.getHost())));
            }
        }

        if (!spBean.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, spBean.getStatus())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, spBean.getMode())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getSpDetail().getServiceProviderId());
                object.put("accountId", getDataManager().getAccountId());
                object.put("residentId", getDataManager().getSpDetail().getResidentId());
                object.put("premiseHierarchyDetailsId", getDataManager().getSpDetail().getFlatId());
                object.put("enteredVehicleNo", getDataManager().getSpDetail().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getSpDetail().getBodyTemperature());
                object.put("enteredVehicleModel", getDataManager().getSpDetail().getEnteredVehicleModel());
                object.put("type", AppConstants.SERVICE_PROVIDER);
                if (getDataManager().getSpDetail().getVehicleBitMapImage() != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getSpDetail().getVehicleBitMapImage()));
                }
                //  object.put("type", AppConstants.SERVICE_PROVIDER);
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
                object.put("id", getDataManager().getSpDetail().getServiceProviderId());
                object.put("enteredVehicleNo", getDataManager().getSpDetail().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getSpDetail().getBodyTemperature());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.SERVICE_PROVIDER);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy", isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("enteredVehicleModel", getDataManager().getSpDetail().getEnteredVehicleModel());
                object.put("rejectReason", input);
                if (getDataManager().getSpDetail().getVehicleBitMapImage() != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getSpDetail().getVehicleBitMapImage()));
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
