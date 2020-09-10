package com.evisitor.ui.main.activity.checkout;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.HouseKeepingCheckInResponse;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.ServiceProviderResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CalenderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutViewModel extends BaseViewModel<ActivityNavigator> {

    public CheckOutViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getCheckOutData(int page, String search, int listOf) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.CHECK_OUT);

            switch (listOf) {
                case 0:
                    getGuestList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedGuest", page + " : " + search);
                    break;

                case 1:
                    getHouseKeeperList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedHK", page + " : " + search);
                    break;

                case 2:
                    getServiceProviderList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedSP", page + " : " + search);
                    break;
            }
        }else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
        }
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
                            getNavigator().onExpectedHKSuccess(houseKeepingCheckInResponse.getContent());
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
                            getNavigator().onExpectedSPSuccess(serviceProviderCheckInResponse.getContent());
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

    List<VisitorProfileBean> getGuestCheckInProfileBean(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(guests.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, guests.getEnteredVehicleNo().isEmpty() ? "N/A" : guests.getEnteredVehicleNo())));
        if (!guests.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo())));
        if (!guests.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, guests.getHouseNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost())));
        if (guests.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, guests.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getHouseKeepingCheckInProfileBean(HouseKeeping houseKeeping) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        HouseKeepingResponse.ContentBean hkBean = new HouseKeepingResponse.ContentBean();
        hkBean.setId(Integer.parseInt(houseKeeping.getHouseKeeperId()));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, houseKeeping.getName())));
        if (!houseKeeping.getProfile().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, houseKeeping.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(houseKeeping.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(houseKeeping.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        if (!houseKeeping.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, houseKeeping.getContactNo())));
        if (!houseKeeping.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, houseKeeping.getIdentityNo())));
        if (!houseKeeping.getHouseNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, houseKeeping.getHouseNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, houseKeeping.getHost().isEmpty() ? houseKeeping.getCreatedBy() : houseKeeping.getHost())));
        if (houseKeeping.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, houseKeeping.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getServiceProviderCheckInProfileBean(ServiceProvider serviceProvider) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(serviceProvider.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, serviceProvider.getEnteredVehicleNo().isEmpty() ? "N/A" : serviceProvider.getEnteredVehicleNo())));
        if (!serviceProvider.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, serviceProvider.getContactNo())));
        if (!serviceProvider.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, serviceProvider.getIdentityNo())));
        if (!serviceProvider.getHouseNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, serviceProvider.getHouseNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, serviceProvider.getHost().isEmpty() ? serviceProvider.getCreatedBy() : serviceProvider.getHost())));
        if (serviceProvider.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, serviceProvider.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }
}
