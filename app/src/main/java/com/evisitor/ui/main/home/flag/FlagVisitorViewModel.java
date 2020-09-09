package com.evisitor.ui.main.home.flag;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.FlaggedVisitorResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlagVisitorViewModel extends BaseViewModel<FlagVisitorNavigator> {
    public FlagVisitorViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getData(int page, String search) {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.FLAG);
            getDataManager().doGetALLFlagVisitors(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            FlaggedVisitorResponse visitorResponse = getDataManager().getGson().fromJson(response.body().string(), FlaggedVisitorResponse.class);
                            if (visitorResponse.getContent() != null)
                                getNavigator().onSuccessFlagList(visitorResponse.getContent());
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
        }else getNavigator().hideSwipeToRefresh();
    }
}
