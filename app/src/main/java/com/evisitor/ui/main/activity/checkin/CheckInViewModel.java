package com.evisitor.ui.main.activity.checkin;

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
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInViewModel extends BaseCheckInOutViewModel<ActivityNavigator> {

    public CheckInViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getGuestListData(int page, String search,int listOf) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));
        map.put("type",AppConstants.CHECK_IN);

        switch (listOf){
            case 0:
                getGuestList(map);
                AppLogger.d("Searching : ExpectedGuest", page + " : " + search);
                break;

            case 1:
                getHouseKeeperList(map);
                AppLogger.d("Searching : ExpectedHK", page + " : " + search);
                break;

            case 2:
                getServiceProviderList(map);
                AppLogger.d("Searching : ExpectedSP", page + " : " + search);
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

    List<VisitorProfileBean> getGuestCheckInProfileBean(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIME_FORMAT_AM))));

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
        getDataManager().setHouseKeeping(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, houseKeeping.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(houseKeeping.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIME_FORMAT_AM))));

        if (!houseKeeping.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, houseKeeping.getContactNo())));
        if (!houseKeeping.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, houseKeeping.getIdentityNo())));
        if (!houseKeeping.getHouseNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, houseKeeping.getHouseNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, houseKeeping.getHost().isEmpty() ? houseKeeping.getCreatedBy() : houseKeeping.getHost())));
        if (!houseKeeping.getProfile().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, houseKeeping.getProfile())));
        if (houseKeeping.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, houseKeeping.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getServiceProviderCheckInProfileBean(ServiceProvider serviceProvider) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setSPDetail(serviceProvider);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, serviceProvider.getEnteredVehicleNo().isEmpty() ? "N/A" : serviceProvider.getEnteredVehicleNo())));
        if (!serviceProvider.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, serviceProvider.getContactNo())));
        if (!serviceProvider.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, serviceProvider.getIdentityNo())));
        if (!serviceProvider.getHouseNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, serviceProvider.getHouseNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, serviceProvider.getHost().isEmpty() ? serviceProvider.getCreatedBy() : serviceProvider.getHost())));
        if (!serviceProvider.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, serviceProvider.getIdentityNo())));
        if (serviceProvider.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, serviceProvider.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void checkOut(int type) {
        JSONObject object = new JSONObject();
        try {
            switch (type){
                case 0:
                    object.put("id",getDataManager().getGuestDetail().getGuestId());
                    object.put("visitor",AppConstants.GUEST);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 1:
                    object.put("id", String.valueOf(getDataManager().getHouseKeeping().getId()));
                    object.put("visitor",AppConstants.HOUSE_HELP);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 2:
                    object.put("id", getDataManager().getSpDetail().getServiceProviderId());
                    object.put("visitor",AppConstants.SERVICE_PROVIDER);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON,object.toString());
        doCheckInOut(body, () -> getNavigator().refreshList());
    }
}
