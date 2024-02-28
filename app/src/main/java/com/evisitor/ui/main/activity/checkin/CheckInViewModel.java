package com.evisitor.ui.main.activity.checkin;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.HouseKeepingCheckInResponse;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.ServiceProviderResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkin.adapter.PrinterStatus;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInViewModel extends BaseCheckInOutViewModel<ActivityNavigator> {

    MutableLiveData<String> propertyImage = new MutableLiveData<>();

    MutableLiveData<String> visitorImage = new MutableLiveData<>();
    private final MutableLiveData<PrinterStatus> _printerStatus = new MutableLiveData<>(PrinterStatus.DISCONNECTED);

    public LiveData<PrinterStatus> getPrinterStatus() {
        return _printerStatus;
    }

    public void setPrinterStatus(PrinterStatus printerStatus) {
        _printerStatus.setValue(printerStatus);
    }
    public CheckInViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getCheckInData(int page, String search, int listOf) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.CHECK_IN);
            map.put("userMasterId", String.valueOf(getDataManager().getUserDetail().getId()));
            switch (listOf) {
                case 0:
                    if (getDataManager().isCommercial()) {
                        getCommercialGuestList(page, map);
                    } else getGuestList(page, map);
                    AppLogger.d("Searching : CheckInViewModel ExpectedGuest", page + " : " + search);
                    break;

                case 1:
                    if (getDataManager().isCommercial())
                        getCommercialStaffList(page, map);
                    else getHouseKeeperList(page, map);
                    AppLogger.d("Searching : CheckInViewModel ExpectedHK", page + " : " + search);
                    break;

                case 2:
                    getServiceProviderList(page, map);
                    AppLogger.d("Searching : CheckInViewModel ExpectedSP", page + " : " + search);
                    break;
            }
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    private void getServiceProviderList(int page, Map<String, String> map) {
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
                            getNavigator().onExpectedSPSuccess(page, serviceProviderCheckInResponse.getContent());
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

    private void getHouseKeeperList(int page, Map<String, String> map) {
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
                            getNavigator().onExpectedHKSuccess(page, houseKeepingCheckInResponse.getContent());
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


    private void getCommercialStaffList(int page, Map<String, String> map) {
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
                            getNavigator().onExpectedOfficeSuccess(page, officeStaffResponse.getContent());
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

    private void getGuestList(int page, Map<String, String> map) {
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
                            getNavigator().onExpectedGuestSuccess(page, guestsResponse.getContent());
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

    private void getCommercialGuestList(int page, Map<String, String> map) {
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
                            getNavigator().onExpectedCommercialGuestSuccess(page, commercialGuestResponse.getContent());
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

    void checkOut(int type) {
        JSONObject object = new JSONObject();
        try {
            switch (type) {
                case 0:
                    if (getDataManager().isCommercial()) {
                        object.put("accountId", getDataManager().getAccountId());
                        object.put("id", getDataManager().getCommercialVisitorDetail().getGuestId());
                        object.put("userMasterId",getDataManager().getUserDetail().getId());
                        object.put("name",getDataManager().getCommercialVisitorDetail().getName());
                        object.put("premiseHierarchyDetailsId",getDataManager().getCommercialVisitorDetail().getFlatId2());
                        object.put("documentId",getDataManager().getCommercialVisitorDetail().getIdentityNo());
                    } else {
                        object.put("id", getDataManager().getGuestDetail().getGuestId());
                    }
                    object.put("visitor", AppConstants.GUEST);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 1:
                    object.put("id", String.valueOf(getDataManager().getHouseKeeping().getId()));
                    object.put("visitor", AppConstants.HOUSE_HELP);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;

                case 2:
                    object.put("id", getDataManager().getSpDetail().getServiceProviderId());
                    object.put("visitor", AppConstants.SERVICE_PROVIDER);
                    object.put("type", AppConstants.CHECK_OUT);
                    break;
            }
        } catch (JSONException e) {
            AppLogger.w("CheckInViewModel", e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        doCheckInOut(body, () -> getNavigator().refreshList());
    }

    void staffCheckOut() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getDataManager().getCommercialStaff().getId());
            object.put("accountId", getDataManager().getAccountId());
            object.put("type", AppConstants.CHECK_OUT);
        } catch (Exception e) {
            AppLogger.w("CheckInViewModel", e.toString());
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getNavigator().showLoading();
        getDataManager().doCommercialStaffCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object1 = new JSONObject(response.body().string());
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), object1.getString("result")).setOnPositiveClickListener(alertDialog -> {
                            alertDialog.dismiss();
                            getNavigator().refreshList();
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

    List<VisitorProfileBean> getCommercialGuestProfileBean(CommercialVisitorResponse.CommercialGuest guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setCommercialVisitorDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, guests.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getEnteredVehicleNo())));
        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getDialingCode().concat(guests.getContactNo())))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getIdentityNo()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));

        if (!guests.getHost().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getHost())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getGuestProfileBean(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        if(guests.isVip)
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vip)));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(guests.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, guests.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getEnteredVehicleNo())));

        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getDialingCode().concat(guests.getContactNo())))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getIdentityNo()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getHost())));
        /*if (guests.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, guests.isHostCheckOut())));
       */ getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getCommercialStaffBean(CommercialStaffResponse.ContentBean bean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setCommercialStaff(bean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, bean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_designation, bean.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_staff_id, bean.getEmployeeId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(bean.getEmployeeId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot,
                bean.getTimeIn().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM),
                bean.getTimeOut().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        if (!bean.getEmployment().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.employment, AppUtils.capitaliseFirstLetter(bean.getEmployment()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CommonUtils.paritalEncodeData(CalenderUtils.formatDate(bean.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT)))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, bean.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getEnteredVehicleNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, bean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(bean.getDialingCode()).concat(" ").concat(bean.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, bean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(bean.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(bean.getDocumentId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, bean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getNationality())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), bean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getPremiseName())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getHouseKeepingProfileBean(HouseKeeping houseKeeping) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        HouseKeepingResponse.ContentBean hkBean = new HouseKeepingResponse.ContentBean();
        hkBean.setId(Integer.parseInt(houseKeeping.getHouseKeeperId()));
        getDataManager().setHouseKeeping(hkBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, houseKeeping.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, houseKeeping.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(houseKeeping.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, houseKeeping.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(houseKeeping.getDialingCode()).concat(" ").concat(houseKeeping.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, houseKeeping.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(houseKeeping.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, houseKeeping.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(houseKeeping.getIdentityNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, houseKeeping.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getNationality())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, houseKeeping.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getCompanyName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), houseKeeping.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : houseKeeping.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, houseKeeping.getHost().isEmpty() ? houseKeeping.getCreatedBy() : houseKeeping.getHost())));
       /* if (houseKeeping.isCheckOutFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, houseKeeping.isHostCheckOut())));
       */ getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    List<VisitorProfileBean> getServiceProviderProfileBean(ServiceProvider serviceProvider) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setSPDetail(serviceProvider);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, serviceProvider.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getEnteredVehicleNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, serviceProvider.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, serviceProvider.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) :CommonUtils.paritalEncodeData( "+ ".concat(serviceProvider.getDialingCode()).concat(" ").concat(serviceProvider.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, serviceProvider.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(serviceProvider.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, serviceProvider.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(serviceProvider.getIdentityNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, serviceProvider.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getNationality())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_comp_name, serviceProvider.getCompanyName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getCompanyName())));
        if (!getDataManager().isCommercial()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), serviceProvider.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getPremiseName())));

            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, serviceProvider.getHost().isEmpty() ? serviceProvider.getCreatedBy() : serviceProvider.getHost())));
            /*if (serviceProvider.isCheckOutFeature())
                visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_is_checkout, serviceProvider.isHostCheckOut())));*/
        }
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    private final MutableLiveData<PropertyInfoResponse> propertyInfoResponseMutableData = new MutableLiveData<>();

    MutableLiveData<PropertyInfoResponse> getPropertyInfo() {
        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());

            getDataManager().doGetPropertyInfo(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            PropertyInfoResponse propertyInfoResponse = getDataManager().getGson().fromJson(response.body().string(), PropertyInfoResponse.class);
                            propertyInfoResponseMutableData.setValue(propertyInfoResponse);
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else getNavigator().handleApiError(response.errorBody());
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        }
        return propertyInfoResponseMutableData;
    }

    public MutableLiveData<String> getPropertyImage() {
        return propertyImage;
    }

    public MutableLiveData<String> getVisitorImage() {
        return visitorImage;
    }

    public void getImage(String image, boolean isProperty){
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("imageName", image);
            getDataManager().doGetBase64ImageByName(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            JSONObject object = new JSONObject(response.body().string());
                            if(isProperty)
                                propertyImage.setValue(object.getString("imageBase64"));
                            else visitorImage.setValue(object.getString("imageBase64"));
                        } else if (response.code() == 401) {
                            getNavigator().openActivityOnTokenExpire();
                        } else {/*getNavigator().handleApiError(response.errorBody());*/}
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                    getNavigator().hideSwipeToRefresh();
                }
            });
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }


}
