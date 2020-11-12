package com.evisitor.ui.main.home.guest.expected;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
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

public class ExpectedGuestViewModel extends BaseCheckInOutViewModel<ExpectedGuestNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    public ExpectedGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getGuestListData(int page, String search) {
        if (getNavigator().isNetworkConnected()){
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type",AppConstants.EXPECTED);
            AppLogger.d("Searching : ExpectedGuest", page + " : " + search);

            getDataManager().doGetExpectedGuestListDetail(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            GuestsResponse guestsResponse = getDataManager().getGson().fromJson(response.body().string(), GuestsResponse.class);
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
        }else {
            getNavigator().hideLoading();
            getNavigator().hideSwipeToRefresh();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile,guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getIdentityNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? guests.getCreatedBy() : guests.getHost())));
        if (guests.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, guests.isHostCheckOut())));
        if (!guests.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, guests.getStatus())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getGuestDetail().getGuestId());
                object.put("accountId", getDataManager().getAccountId());
                object.put("residentId", getDataManager().getGuestDetail().getResidentId());
                object.put("premiseHierarchyDetailsId", getDataManager().getGuestDetail().getFlatId());
                object.put("enteredVehicleNo", getDataManager().getGuestDetail().getEnteredVehicleNo());
                object.put("type", AppConstants.GUEST);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            sendNotification(body, this);
        }
    }

    void approveByCall(boolean isAccept,String input) {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getGuestDetail().getGuestId());
                object.put("enteredVehicleNo", getDataManager().getGuestDetail().getEnteredVehicleNo());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy",isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("rejectReason",input);
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
