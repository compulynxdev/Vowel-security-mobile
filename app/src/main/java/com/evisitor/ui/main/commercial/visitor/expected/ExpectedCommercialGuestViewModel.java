package com.evisitor.ui.main.commercial.visitor.expected;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
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
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo()))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getIdentityNo())));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        if (!guests.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, guests.getStatus())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                object.put("accountId", getDataManager().getAccountId());
                object.put("staffId", getDataManager().getCommercialVisitorDetail().getStaffId());
                object.put("premiseHierarchyDetailsId", getDataManager().getCommercialVisitorDetail().getFlatId());
                object.put("enteredVehicleNo", getDataManager().getCommercialVisitorDetail().getEnteredVehicleNo());
                JSONArray deviceList = new JSONArray(new Gson().toJson(getDataManager().getCommercialVisitorDetail().getDeviceBeanList()));
                object.put("deviceList", deviceList);
                //object.put("type", AppConstants.GUEST);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            sendCommercialNotification(body, this);
        }
    }

    void approveByCall(boolean isAccept, String input) {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                object.put("enteredVehicleNo", getDataManager().getCommercialVisitorDetail().getEnteredVehicleNo());
                object.put("accountId", getDataManager().getAccountId());
                JSONArray deviceList = new JSONArray(new Gson().toJson(getDataManager().getCommercialVisitorDetail().getDeviceBeanList()));
                object.put("deviceList", deviceList);
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy", isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("rejectReason", input);
            } catch (JSONException e) {
                e.printStackTrace();
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
