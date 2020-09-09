package com.evisitor.ui.main.home.trespasser.guests;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrespasserGuestViewModel extends BaseViewModel<TrespasserGuestNavigator> {
    public TrespasserGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getTrespasserGuest(int page, String search) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));
        map.put("type", AppConstants.TODAY);
        AppLogger.d("Searching : Trespasser", page + " : " + search);

        getDataManager().doGetAllTrespasserGuest(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        TrespasserResponse registeredHKResponse = getDataManager().getGson().fromJson(response.body().string(), TrespasserResponse.class);
                        if (registeredHKResponse.getContent() != null) {
                            AppLogger.d("Searching : Size", "" + registeredHKResponse.getContent().size());
                            getNavigator().onTrespasserSuccess(registeredHKResponse.getContent());
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
    }

}
