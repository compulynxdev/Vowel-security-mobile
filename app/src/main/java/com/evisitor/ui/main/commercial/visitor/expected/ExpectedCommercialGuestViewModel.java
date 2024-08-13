package com.evisitor.ui.main.commercial.visitor.expected;

import android.util.Log;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
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

public class ExpectedCommercialGuestViewModel extends BaseCheckInOutViewModel<ExpectedCommercialGuestNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    public ExpectedCommercialGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getExpectedVisitorListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.EXPECTED);
            map.put("userMasterId", String.valueOf(getDataManager().getUserDetail().getId()));
            AppLogger.d("Searching : ExpectedGuest", page + " : " + search);

            getDataManager().doGetExpectedCommercialGuestListDetail(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            CommercialVisitorResponse guestsResponse = getDataManager().getGson().fromJson(response.body().string(), CommercialVisitorResponse.class);
                            if (guestsResponse.getContent() != null) {
                                getNavigator().onExpectedGuestSuccess(guestsResponse.getContent());
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
            getNavigator().hideLoading();
            getNavigator().hideSwipeToRefresh();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(CommercialVisitorResponse.CommercialGuest guests) {
        getNavigator().showLoading();
        getDataManager().setCommercialVisitorDetail(guests);
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo())))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getIdentityNo()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getHost())));
        if (!guests.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, guests.getStatus())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification(List<CheckInTemperature> guestIds) {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                object.put("accountId", getDataManager().getAccountId());
                object.put("staffId", getDataManager().getCommercialVisitorDetail().getStaffId());
                JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                object.put("guestIdList", ids);
                object.put("documentId",getDataManager().getCommercialVisitorDetail().getIdentityNo());
                object.put("mode",getDataManager().getCommercialVisitorDetail().getMode());
                object.put("type",AppConstants.GUEST);
                object.put("premiseHierarchyDetailsId", getDataManager().getCommercialVisitorDetail().getFlatId());
                object.put("enteredVehicleNo", getDataManager().getCommercialVisitorDetail().getEnteredVehicleNo());
                object.put("enteredVehicleModel", getDataManager().getCommercialVisitorDetail().getEnteredVehicleModel());
                object.put("bodyTemperature", getDataManager().getCommercialVisitorDetail().getBodyTemperature());
                JSONArray deviceList = new JSONArray(new Gson().toJson(getDataManager().getCommercialVisitorDetail().getDeviceBeanList()));
                object.put("deviceList", deviceList);
            } catch (JSONException e) {
                AppLogger.w("ExpectedCommercialGuestViewModel", e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            sendCommercialNotification(body, this);
        }
    }

    void acceptIfCheckInISApproved(List<CheckInTemperature> guestIds){
        if (getNavigator().isNetworkConnected(true)){
            JSONObject object = new JSONObject();
            try{
                object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                object.put("documentId", getDataManager().getCommercialVisitorDetail().getIdentityNo());
                object.put("premiseHierarchyDetailsId", getDataManager().getCommercialVisitorDetail().getFlatId());
                String accountId = getDataManager().getAccountId();
                object.put("accountId", accountId);
                object.put("mode", getDataManager().getCommercialVisitorDetail().getMode());
                object.put("staffId", getDataManager().getCommercialVisitorDetail().getStaffId());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                object.put("state", AppConstants.ACCEPT);
                JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                object.put("guestIdList", ids);
                object.put("userMasterId", getDataManager().getUserDetail().getId());
                object.put("name",getDataManager().getCommercialVisitorDetail().getName());
                Log.e( "accepheckInISApproved: ","" );
            }catch (Exception e) {
                AppLogger.w("ExpectedCommercialGuestViewModel", e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            doCheckInOut(body, this);
        }
    }

    void approveByCall(boolean isAccept, String input,List<CheckInTemperature> guestIds) {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                object.put("enteredVehicleNo", getDataManager().getCommercialVisitorDetail().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getCommercialVisitorDetail().getBodyTemperature());
                object.put("accountId", getDataManager().getAccountId());
                JSONArray deviceList = new JSONArray(new Gson().toJson(getDataManager().getCommercialVisitorDetail().getDeviceBeanList()));
                object.put("deviceList", deviceList);
                JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                object.put("guestIdList", ids);
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy", isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("rejectReason", input);
                object.put("premiseHierarchyDetailsId", getDataManager().getCommercialVisitorDetail().getFlatId());
                object.put("documentId", getDataManager().getCommercialVisitorDetail().getIdentityNo());
                object.put("mode", getDataManager().getCommercialVisitorDetail().getMode());
                object.put("staffId", getDataManager().getCommercialVisitorDetail().getStaffId());
                object.put("userMasterId", getDataManager().getUserDetail().getId());
                object.put("name",getDataManager().getCommercialVisitorDetail().getName());
            } catch (JSONException e) {
                AppLogger.w("ExpectedCommercialGuestViewModel", e.toString());
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
