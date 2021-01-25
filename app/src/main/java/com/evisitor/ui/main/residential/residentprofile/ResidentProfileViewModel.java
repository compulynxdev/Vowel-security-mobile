package com.evisitor.ui.main.residential.residentprofile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.dialog.AlertDialog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResidentProfileViewModel extends BaseViewModel<ResidentProfileNavigator> {
    private MutableLiveData<ResidentProfile> profileLiveData = new MutableLiveData<>();

    public ResidentProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<ResidentProfile> getResidentData(String qrCode) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("type", "expected");
            if (!qrCode.isEmpty())
                map.put("qrCode", qrCode);
            getDataManager().doGetCommercialStaffByQRCode(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            ResidentProfile residentProfile = getDataManager().getGson().fromJson(response.body().string(), ResidentProfile.class);
                            if (residentProfile != null) {
                                profileLiveData.setValue(residentProfile);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data).setOnPositiveClickListener(dialog -> {
                                    dialog.dismiss();
                                    getNavigator().openNextActivity();
                                });
                            }
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else getNavigator().handleApiError(response.errorBody());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();
                            getNavigator().openNextActivity();
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet))
                    .setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                        @Override
                        public void onPositiveClick(AlertDialog dialog) {
                            dialog.dismiss();
                            getNavigator().openNextActivity();
                        }
                    });
        }
        return profileLiveData;
    }


    public MutableLiveData<ResidentProfile> getProfileLiveData() {
        return profileLiveData;
    }

}
