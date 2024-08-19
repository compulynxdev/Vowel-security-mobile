package com.evisitor.ui.main.residential.guest.expected;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.ResidentProfile;
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

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectedGuestViewModel extends BaseCheckInOutViewModel<ExpectedGuestNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    private static final String TAG = "ExpectedGuestViewModel";

    public ExpectedGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getGuestListData(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("userMasterId", String.valueOf(getDataManager().getUserDetail().getId()));
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.EXPECTED);
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
        } else {
            getNavigator().hideLoading();
            getNavigator().hideSwipeToRefresh();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        if (guests.isVip)
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vip)));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo())))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getIdentityNo()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? guests.getCreatedBy() : guests.getHost())));
       /* if (guests.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, guests.isHostCheckOut())));
       */
        if (!guests.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, guests.getStatus())));

        if (guests.getMode() != null && !guests.getMode().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, guests.getMode())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification(List<CheckInTemperature> guestIds) {
        if ((!getDataManager().getGuestDetail().getExpectedVehicleNo().isEmpty() || !getDataManager().getGuestDetail().getEnteredVehicleNo().isEmpty())
                && getDataManager().getGuestDetail().getNo_plate_bmp_img() == null) {
            getNavigator().showAlert(R.string.alert, R.string.please_capture_vehical_image);
        } else {
            if (getNavigator().isNetworkConnected(true)) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", getDataManager().getGuestDetail().getGuestId());
                    object.put("accountId", getDataManager().getAccountId());
                    object.put("residentId", getDataManager().getGuestDetail().getResidentId());
                    JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                    object.put("guestIdList", ids);
                    object.put("premiseHierarchyDetailsId", getDataManager().getGuestDetail().getFlatId());
                    object.put("enteredVehicleNo", getDataManager().getGuestDetail().getEnteredVehicleNo());
                    object.put("enteredVehicleModel", getDataManager().getGuestDetail().getEnteredVehicleModel());
                    object.put("bodyTemperature", getDataManager().getGuestDetail().getBodyTemperature());
                    object.put("type", AppConstants.GUEST);
                    if (getDataManager().getGuestDetail().getNo_plate_bmp_img() != null) {
                        object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getGuestDetail().getNo_plate_bmp_img()));
                    }
                } catch (JSONException e) {
                    AppLogger.w(TAG, e.toString());
                }

                RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
                sendNotification(body, this);
            }
        }
    }

    void approveByCall(boolean isAccept, String input, List<CheckInTemperature> guestIds) {
        /*if((!getDataManager().getGuestDetail().getExpectedVehicleNo().isEmpty() || !getDataManager().getGuestDetail().getEnteredVehicleNo().isEmpty())
                && getDataManager().getGuestDetail().getNo_plate_bmp_img()==null){
            getNavigator().showToast(R.string.please_capture_vehical_image);
       }else if(getDataManager().getGuestDetail().getMode().equalsIgnoreCase("Drive-In") && (getDataManager().getGuestDetail().getEnteredVehicleNo().isEmpty() ||
               getDataManager().getGuestDetail().getExpectedVehicleNo().isEmpty())){
            getNavigator().showToast(R.string.please_enter_vehical_no);
        }else {*/
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getGuestDetail().getGuestId());
                object.put("enteredVehicleNo", getDataManager().getGuestDetail().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getGuestDetail().getBodyTemperature());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                object.put("guestIdList", ids);
                object.put("state", isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("rejectedBy", isAccept ? null : getDataManager().getUserDetail().getFullName());
                object.put("enteredVehicleModel", getDataManager().getGuestDetail().getEnteredVehicleModel());
                object.put("rejectReason", input);
                if (getDataManager().getGuestDetail().getNo_plate_bmp_img() != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getGuestDetail().getNo_plate_bmp_img()));
                }
            } catch (JSONException e) {
                AppLogger.w(TAG, e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            doCheckInOut(body, this);
        }
        //}
    }

    @Override
    public void onSuccess() {
        getNavigator().refreshList();
    }
}
