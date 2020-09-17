package com.evisitor.ui.main.notifications;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.NotificationResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends BaseViewModel<NotificationNavigator> {
    public NotificationsViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getNotifications(int page, String search)
    {
        if (getNavigator().isNetworkConnected()){
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("username", "" + getDataManager().getUsername());
            map.put("size", String.valueOf(AppConstants.LIMIT));
            AppLogger.d("Searching : NotificationResponse", page + " : " + search);

            getDataManager().doGetNotifications(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            NotificationResponse notificationResponse = getDataManager().getGson().fromJson(response.body().string(), NotificationResponse.class);
                            if (notificationResponse.getContent() != null) {
                                getNavigator().onNotificationSuccess(notificationResponse.getContent());
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

}
