package com.evisitor.ui.main.home.trespasser;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrespasserViewModel extends BaseViewModel<BaseNavigator> {
    public TrespasserViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<Boolean> isNotificationEnable = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsNotificationEnable() {
        return isNotificationEnable;
    }

    void doGetDeviceNotificationStatus() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", getDataManager().getUserId());
            object.put("accountId", getDataManager().getAccountId());
        } catch (Exception e) {
            AppLogger.w("Trespasser", e.toString());
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getNavigator().showLoading();
       getDataManager().doGetDeviceNotificationStatus(getDataManager().getHeader(),body).enqueue(new Callback<Boolean>() {
           @Override
           public void onResponse(@NonNull  Call<Boolean> call,@NonNull Response<Boolean> response) {
               getNavigator().hideLoading();
               if (response.code() == 200) {
                   try {
                       isNotificationEnable.setValue(response.body());
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
           public void onFailure(@NonNull Call<Boolean> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
           }
       });
    }

    void doEnableDeviceNotification() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("userId", getDataManager().getUserId());
            object.put("accountId", getDataManager().getAccountId());
            object.put("enable", !isNotificationEnable.getValue());
        } catch (Exception e) {
            AppLogger.w("Trespasser", e.toString());
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getNavigator().showLoading();
        getDataManager().doEnableDeviceNotification(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    try {
                        isNotificationEnable.setValue(!isNotificationEnable.getValue());
                        if(isNotificationEnable.getValue()){
                            getNavigator().showAlert(R.string.alert,R.string.notification_enabled).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                                @Override
                                public void onPositiveClick(AlertDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            getNavigator().showAlert(R.string.alert,R.string.notification_disabled).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                                @Override
                                public void onPositiveClick(AlertDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }

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
