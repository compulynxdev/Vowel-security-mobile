package com.evisitor.ui.main.activity.checkin;

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
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.main.home.guest.expected.GuestNavigator;
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

public class CheckInViewModel extends BaseViewModel<GuestNavigator> {

    private MutableLiveData<List<Guests>> guestListData = new MutableLiveData<>();
    private MutableLiveData<List<HouseKeeping>> houseKeepingListData = new MutableLiveData<>();
    private MutableLiveData<List<ServiceProvider>> serviceProviderListData = new MutableLiveData<>();

    public CheckInViewModel(DataManager dataManager) {
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
        map.put("type",AppConstants.CHECK_IN);

        switch (listOf){
            case 0:
                getGuestList(map);
                break;

            case 1:
                getHouseKeeperList(map);
                break;

            case 2:
                getServiceProviderList(map);
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

    List<VisitorProfileBean> getGuestCheckInProfileBean(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIME_FORMAT_AM))));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), true));
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
        Guests guests = new Guests();
        guests.setGuestId(houseKeeping.getHouseKeeperId());
        getDataManager().setGuestDetail(guests);
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
        Guests guests = new Guests();
        guests.setGuestId(serviceProvider.getServiceProviderId());
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), serviceProvider.getExpectedVehicleNo(), true));
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
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            switch (type){
                case 0:
                    object.put("id",getDataManager().getGuestDetail().getGuestId());
                    object.put("enteredVehicleNo",getDataManager().getGuestDetail().getExpectedVehicleNo());
                    object.put("visitor",AppConstants.GUEST);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 1:
                    object.put("id",getDataManager().getGuestDetail().getGuestId());
                    object.put("enteredVehicleNo",getDataManager().getGuestDetail().getExpectedVehicleNo());
                    object.put("visitor",AppConstants.HOUSE_HELP);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 2:
                    object.put("id",getDataManager().getGuestDetail().getGuestId());
                    object.put("enteredVehicleNo",getDataManager().getGuestDetail().getExpectedVehicleNo());
                    object.put("visitor",AppConstants.SERVICE_PROVIDER);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON,object.toString());
        getDataManager().doCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull  Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200){
                    try {
                        assert response.body()!=null;
                        JSONObject object1 = new JSONObject(response.body().string());
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess),object1.getString("result")).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();
                            getNavigator().refreshList();
                        });
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                }else if (response.code()==401){
                    getNavigator().openActivityOnTokenExpire();
                }  else{
                    getNavigator().handleApiError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }
}
