package com.evisitor.ui.main.home.blacklist;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.BlackListVisitorResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlackListViewModel extends BaseViewModel<BlackListNavigtor> {
    public BlackListViewModel(DataManager dataManager) {
        super(dataManager);
    }


    void getData(int page, String search){
        AppLogger.e("MYPage", page + " : " + search);
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));
        map.put("type",AppConstants.BLACK);
        AppLogger.d("Searching : BlackList", "" + page);
        getDataManager().doGetBlackListVisitors(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        BlackListVisitorResponse visitorResponse = getDataManager().getGson().fromJson(response.body().string(), BlackListVisitorResponse.class);
                        if (visitorResponse.getContent() != null)
                            getNavigator().OnSuccessBlackList(visitorResponse.getContent());
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

    }
}
