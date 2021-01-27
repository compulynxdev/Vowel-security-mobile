package com.evisitor.ui.main.activity.checkout;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.HouseKeepingCheckInResponse;
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
                    if (getDataManager().isCommercial())
                        getCommercialVisitorList(map);
                    else getGuestList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedGuest", page + " : " + search);
                    break;

                case 1:
                    if (getDataManager().isCommercial())
                        getCommercialStaffList(map);
                    else getHouseKeeperList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedHK", page + " : " + search);
                    break;

                case 2:
                    getServiceProviderList(map);
                    AppLogger.d("Searching : CheckOutViewModel ExpectedSP", page + " : " + search);
                    break;
            }
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
        }
    }

    private void getCommercialVisitorList(Map<String, String> map) {
        getDataManager().doGetCommercialGuestCheckInOutList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        CommercialVisitorResponse commercialGuestResponse = getDataManager().getGson().fromJson(response.body().string(), CommercialVisitorResponse.class);
                        if (commercialGuestResponse.getContent() != null) {
                            getNavigator().onExpectedCommercialGuestSuccess(commercialGuestResponse.getContent());
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
        getDataManager().doGetGuestCheckInOutList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
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

    private void getCommercialStaffList(Map<String, String> map) {
        getDataManager().doGetCommercialStaffCheckInOutListDetail(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        CommercialStaffResponse officeStaffResponse = getDataManager().getGson().fromJson(response.body().string(), CommercialStaffResponse.class);
                        if (officeStaffResponse.getContent() != null) {
                            getNavigator().onExpectedOfficeSuccess(officeStaffResponse.getContent());
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
        getDataManager().doGetHouseKeepingCheckInOutList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
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
        getDataManager().doGetServiceProviderCheckInOutList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
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

    List<VisitorProfileBean> getCommercialGuestProfileBean(CommercialVisitorResponse.CommercialGuest guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setCommercialVisitorDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(guests.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, guests.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getEnteredVehicleNo())));

        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo()))));

        if (getDataManager().isIdentifyFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getIdentityNo())));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));

        if (!guests.getHost().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getHost())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getGuestProfileBean(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(guests.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, guests.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getEnteredVehicleNo())));

        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo()))));

        if (getDataManager().isIdentifyFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getIdentityNo())));

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getHost())));
        if (guests.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, guests.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getCommercialStaffProfileBean(CommercialStaffResponse.ContentBean bean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, bean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_designation, bean.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_staff_id, bean.getEmployeeId())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot,
                bean.getTimeIn().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM),
                bean.getTimeOut().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        if (!bean.getEmployment().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.employment, bean.getEmployment())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(bean.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(bean.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, bean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(bean.getDialingCode()).concat(" ").concat(bean.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getDocumentId())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), bean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getPremiseName())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getHouseKeepingProfileBean(HouseKeeping houseKeeping) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, houseKeeping.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, houseKeeping.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(houseKeeping.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(houseKeeping.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, houseKeeping.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(houseKeeping.getDialingCode()).concat(" ").concat(houseKeeping.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, houseKeeping.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getIdentityNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, houseKeeping.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getCompanyName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), houseKeeping.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, houseKeeping.getHost().isEmpty() ? houseKeeping.getCreatedBy() : houseKeeping.getHost())));
        if (houseKeeping.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, houseKeeping.isHostCheckOut())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getServiceProviderProfileBean(ServiceProvider serviceProvider) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_out, CalenderUtils.formatDate(serviceProvider.getCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, serviceProvider.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getEnteredVehicleNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, serviceProvider.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(serviceProvider.getDialingCode()).concat(" ").concat(serviceProvider.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, serviceProvider.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getIdentityNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, serviceProvider.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getCompanyName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), serviceProvider.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getPremiseName())));
        if (!getDataManager().isCommercial()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, serviceProvider.getHost().isEmpty() ? serviceProvider.getCreatedBy() : serviceProvider.getHost())));
            if (serviceProvider.isCheckOutFeature())
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, serviceProvider.isHostCheckOut())));
        }
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }
}
