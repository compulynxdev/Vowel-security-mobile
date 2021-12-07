package com.evisitor.ui.main.residential.residentprofile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ResidentProfileViewModel extends BaseViewModel<ResidentProfileNavigator> {
    private final MutableLiveData<ResidentProfile> profileLiveData = new MutableLiveData<>();

    public ResidentProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }


    void setProfileLiveData(ResidentProfile profile) {
        profileLiveData.setValue(profile);
    }

    public MutableLiveData<ResidentProfile> getProfileLiveData() {
        return profileLiveData;
    }

    public void doCheckInCheckOut(boolean isCheckIn, ResidentProfile profile, String vehicleNo) {
        getNavigator().showLoading();
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                if (isCheckIn)
                    object.put("residentId", profile.getId());
                else object.put("id", profile.getPrimaryId());
                object.put("vehicleNo", vehicleNo);
                object.put("type", isCheckIn ? AppConstants.CHECK_IN : AppConstants.CHECK_OUT);
            } catch (JSONException e) {
                AppLogger.w("Resident", e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getNavigator().showLoading();
            getDataManager().doResidentCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), isCheckIn ? getNavigator().getContext().getString(R.string.check_in_success) :
                                    getNavigator().getContext().getString(R.string.check_out_successfully)
                            ).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                                @Override
                                public void onPositiveClick(AlertDialog dialog) {
                                    dialog.dismiss();
                                    getNavigator().openNext();
                                }
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


}
