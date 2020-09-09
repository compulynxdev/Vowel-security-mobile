package com.evisitor.ui.main;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCheckInOutViewModel<N extends BaseNavigator> extends BaseViewModel {

    private WeakReference<N> mNavigator;

    public BaseCheckInOutViewModel(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public N getNavigator() {
        return mNavigator.get();
    }

    public void setCheckInOutNavigator(N navigator) {
        //noinspection unchecked
        super.setNavigator(navigator);
        this.mNavigator = new WeakReference<>(navigator);
    }

    protected void sendNotification(RequestBody body, ApiCallback apiCallback) {
        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            getDataManager().doGuestSendNotification(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.success)
                                , getNavigator().getContext().getString(R.string.send_notification_success)).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();

                            if (apiCallback != null)
                                apiCallback.onSuccess();
                        });
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

    protected void doCheckInOut(RequestBody body, ApiCallback apiCallback) {
        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            getDataManager().doCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            JSONObject object1 = new JSONObject(response.body().string());
                            getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), object1.getString("result")).setOnPositiveClickListener(dialog -> {
                                dialog.dismiss();
                                if (apiCallback != null)
                                    apiCallback.onSuccess();
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

    public interface ApiCallback {
        void onSuccess();
    }
}
