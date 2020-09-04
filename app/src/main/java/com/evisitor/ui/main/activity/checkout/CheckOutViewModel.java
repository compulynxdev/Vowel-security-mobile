package com.evisitor.ui.main.activity.checkout;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.HouseKeepingCheckInResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.ServiceProviderResponse;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestNavigator;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutViewModel extends BaseViewModel<ExpectedGuestNavigator> {

    private MutableLiveData<List<Guests>> guestListData = new MutableLiveData<>();
    private MutableLiveData<List<HouseKeeping>> houseKeepingListData = new MutableLiveData<>();
    private MutableLiveData<List<ServiceProvider>> serviceProviderListData = new MutableLiveData<>();

    public CheckOutViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<Guests>> getGuestListData() {
        return guestListData;
    }

    MutableLiveData<List<HouseKeeping>> getHouseKeepingListData() {
        return houseKeepingListData;
    }

    MutableLiveData<List<ServiceProvider>> getServiceProviderListData() {
        return serviceProviderListData;
    }


    void getGuestListData(int page, String search,int listOf) {
        AppLogger.e("MYPage", page + " : " + search);
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));
        map.put("type",AppConstants.CHECK_OUT);

        switch (listOf){
            case 0:
                getGuestList(map);
                AppLogger.d("Searching : ExpectedGuest", "" + page);
                break;

            case 1:
                getHouseKeeperList(map);
                AppLogger.d("Searching : ExpectedHK", "" + page);
                break;

            case 2:
                getServiceProviderList(map);
                AppLogger.d("Searching : ExpectedSP", "" + page);
                break;
        }

    }

    private void getServiceProviderList(Map<String, String> map) {
        getDataManager().doGetServiceProviderCheckInList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        ServiceProviderResponse serviceProviderCheckInResponse = getDataManager().getGson().fromJson(response.body().string(), ServiceProviderResponse.class);
                        if (serviceProviderCheckInResponse.getContent() != null) {
                            serviceProviderListData.setValue(serviceProviderCheckInResponse.getContent());
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

    private void getHouseKeeperList(Map<String, String> map) {
        getDataManager().doGetHouseKeepingCheckInList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        HouseKeepingCheckInResponse houseKeepingCheckInResponse = getDataManager().getGson().fromJson(response.body().string(), HouseKeepingCheckInResponse.class);
                        if (houseKeepingCheckInResponse.getContent() != null) {
                            houseKeepingListData.setValue(houseKeepingCheckInResponse.getContent());
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

    private void getGuestList(Map<String, String> map) {
        getDataManager().doGetGuestCheckInList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        GuestsResponse guestsResponse = getDataManager().getGson().fromJson(response.body().string(), GuestsResponse.class);
                        if (guestsResponse.getContent() != null) {
                            guestListData.setValue(guestsResponse.getContent());
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
