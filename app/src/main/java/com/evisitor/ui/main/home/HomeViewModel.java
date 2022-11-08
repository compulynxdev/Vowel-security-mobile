package com.evisitor.ui.main.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HomeBean;
import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.main.BaseCheckInOutViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
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

public class HomeViewModel extends BaseCheckInOutViewModel<HomeNavigator> implements BaseCheckInOutViewModel.ApiCallback {

    static final int ADD_VISITOR_VIEW = 0;
    static final int GUEST_VIEW = 1;
    static final int STAFF_VIEW = 2;
    static final int SERVICE_PROVIDER_VIEW = 3;
    static final int TOTAL_VISITOR_VIEW = 4;
    static final int BLACKLISTED_VISITOR_VIEW = 5;
    static final int TRESPASSER_VIEW = 6;
    static final int FLAGGED_VIEW = 7;
    static final int REJECTED_VIEW = 8;
    private static final String TAG = "HomeViewModel";
    private final MutableLiveData<List<HomeBean>> homeListData = new MutableLiveData<>();
    private final MutableLiveData<Integer> notificationCountData = new MutableLiveData<>();
    private final List<HomeBean> list = new ArrayList<>();

    public HomeViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HomeBean>> getHomeListData() {
        return homeListData;
    }

    MutableLiveData<Integer> getNotificationCountData() {
        return notificationCountData;
    }

    void setupHomeList() {
        list.add(new HomeBean(ADD_VISITOR_VIEW, R.drawable.ic_add_button, getNavigator().getContext().getString(R.string.title_add_visitor)));
        list.add(new HomeBean(GUEST_VIEW, R.drawable.ic_guest, getDataManager().isCommercial() ? getNavigator().getContext().getString(R.string.title_visitor) : getNavigator().getContext().getString(R.string.title_guests)));

        if (getDataManager().isCommercial())
            list.add(new HomeBean(STAFF_VIEW, R.drawable.ic_staff, getNavigator().getContext().getString(R.string.title_staff)));
        else
            list.add(new HomeBean(STAFF_VIEW, R.drawable.ic_maid, getNavigator().getContext().getString(R.string.title_domestic_staff)));

        list.add(new HomeBean(SERVICE_PROVIDER_VIEW, R.drawable.ic_assistance, getNavigator().getContext().getString(R.string.title_service_provider)));
        list.add(new HomeBean(TOTAL_VISITOR_VIEW, R.drawable.ic_group, getNavigator().getContext().getString(R.string.title_ttl_expected_visitor)));
        list.add(new HomeBean(BLACKLISTED_VISITOR_VIEW, R.drawable.ic_black_visitor, getNavigator().getContext().getString(R.string.title_blacklisted_visitor)));
        list.add(new HomeBean(TRESPASSER_VIEW, R.drawable.ic_trespasser, getNavigator().getContext().getString(R.string.title_trespasser_visitor)));
        list.add(new HomeBean(FLAGGED_VIEW, R.drawable.ic_flag_visitor, getNavigator().getContext().getString(R.string.title_flagged_visitor)));
        list.add(new HomeBean(REJECTED_VIEW, R.drawable.ic_rejected, getNavigator().getContext().getString(R.string.title_rejected_visitor)));
        homeListData.setValue(list);
    }

    void getVisitorCount() {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("username", getDataManager().getUsername());
            map.put("accountId", getDataManager().getAccountId());

            getDataManager().doGetVisitorCount(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (!list.isEmpty()) {
                                if (jsonObject.has("notification")) {
                                    int notificationCount = jsonObject.getInt("notification");
                                    notificationCountData.setValue(notificationCount);
                                }
                                int guestCount = jsonObject.getInt("guest");
                                int hkCount = jsonObject.getInt("staff");
                                int spCount = jsonObject.getInt("serviceProvider");
                                int commercialStaffCount = jsonObject.has("commercialStaff") ? jsonObject.getInt("commercialStaff") : 0;
                                int totalCount = guestCount + spCount;
                                if (getDataManager().isCommercial())
                                    totalCount = totalCount + commercialStaffCount;
                                else totalCount = totalCount + hkCount;
                                list.get(GUEST_VIEW).setCount(String.valueOf(guestCount));
                                list.get(STAFF_VIEW).setCount(String.valueOf(getDataManager().isCommercial() ? commercialStaffCount : hkCount));
                                list.get(SERVICE_PROVIDER_VIEW).setCount(String.valueOf(spCount));
                                list.get(TOTAL_VISITOR_VIEW).setCount(String.valueOf(totalCount));
                                homeListData.setValue(list);
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
                    getNavigator().handleApiFailure(t);
                }
            });
        }
    }

    void getResidentData(String qrCode) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("type", "expected");
            if (!qrCode.isEmpty())
                map.put("qrCode", qrCode);
            getDataManager().doGetResidentByQRCode(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            ResidentProfile residentProfile = getDataManager().getGson().fromJson(response.body().string(), ResidentProfile.class);
                            if (residentProfile != null) {
                                getNavigator().onSuccessResidentData(residentProfile);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data);
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    public void getQRCodeData(String qrCode) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("type", "guest");
                if (!qrCode.isEmpty())
                    object.put("qrCode", qrCode);
            } catch (JSONException e) {
                AppLogger.w("TAG", e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doGetVisitorByQRCode(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            Guests guests = getDataManager().getGson().fromJson(response.body().string(), Guests.class);
                            if (guests != null) {
                                getNavigator().onSuccessGuestData(guests);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data);
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        if (guests.isVip)
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vip)));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("".concat(guests.getDialingCode()).concat(" ").concat(guests.getContactNo())))));

        if (getDataManager().isIdentifyFeature()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, guests.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(guests.getDocumentType()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(guests.getIdentityNo()))));
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, guests.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getNationality())));
        }

        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? guests.getCreatedBy() : guests.getHost())));
        if (!guests.getStatus().equalsIgnoreCase("PENDING"))
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.status, guests.getStatus())));

        if (guests.getMode() != null && !guests.getMode().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, guests.getMode())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void approveByCall(List<CheckInTemperature> guestIds) {
//        if ((!getDataManager().getGuestDetail().getExpectedVehicleNo().isEmpty() || !getDataManager().getGuestDetail().getEnteredVehicleNo().isEmpty())
//                && getDataManager().getGuestDetail().getNo_plate_bmp_img() == null) {
//            getNavigator().showAlert(R.string.alert, R.string.please_capture_vehical_image);
//        } else {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("id", getDataManager().getGuestDetail().getGuestId());
                object.put("enteredVehicleNo", getDataManager().getGuestDetail().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getGuestDetail().getBodyTemperature());
                object.put("type", AppConstants.CHECK_IN);
                object.put("visitor", AppConstants.GUEST);
                JSONArray ids = new JSONArray(new Gson().toJson(guestIds));
                object.put("guestIdList", ids);
                object.put("state", AppConstants.ACCEPT);
                object.put("rejectedBy", null);
                object.put("rejectReason", "");
                object.put("documentId", getDataManager().getGuestDetail().getIdentityNo());
                object.put("userMasterId", getDataManager().getUserDetail().getId());
                object.put("name", getDataManager().getGuestDetail().getName());
                object.put("accountId", getDataManager().getAccountId());
                String flatId2 = getDataManager().getGuestDetail().getFlatId2();
                if (!flatId2.equals("")) {
                    object.put("premiseHierarchyDetailsId", getDataManager().getGuestDetail().getFlatId2());
                }

                if (getDataManager().getGuestDetail().getNo_plate_bmp_img() != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(getDataManager().getGuestDetail().getNo_plate_bmp_img()));
                }
            } catch (JSONException e) {
                AppLogger.w(TAG, e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            doCheckInOut(body, this);
        }
//        }
    }

    List<VisitorProfileBean> getServiceProviderProfileBean(ServiceProvider serviceProvider) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setSPDetail(serviceProvider);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, serviceProvider.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time, CalenderUtils.formatDate(serviceProvider.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_vehicle, serviceProvider.getEnteredVehicleNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getEnteredVehicleNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, serviceProvider.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : serviceProvider.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, serviceProvider.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("+ ".concat(serviceProvider.getDialingCode()).concat(" ").concat(serviceProvider.getContactNo())))));
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

    public void scanQRIdForRecurrent(String qrCode, boolean isGuest) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("type", isGuest ? "guest" : "sp");
                if (!qrCode.isEmpty())
                    object.put("qrCode", qrCode);
            } catch (JSONException e) {
                AppLogger.w("TAG", e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doGetVisitorByQRCode(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            RecurrentVisitor recurrentVisitor = getDataManager().getGson().fromJson(response.body().string(), RecurrentVisitor.class);
                            if (recurrentVisitor != null) {
                                recurrentVisitor.setVisitorType(isGuest ? AppConstants.GUEST : AppConstants.SERVICE_PROVIDER);
                                getNavigator().onResponseRecurrentData(recurrentVisitor);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data);
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    @Override
    public void onSuccess() {

    }

    public void getQRCodeDataByType(String qr, String type) {
        switch (type) {
            case "guest":
                getQRCodeData(qr);
                break;

            case "serviceprovider":
//                getSpByQr(qr);
                scanQRIdForRecurrent(qr, false);
                break;

            case "recurrentGuest":
                scanQRIdForRecurrent(qr, true);
                break;

            case "resident":
                getResidentData(qr);
                break;

            case "employee":
                getEmployeeScannedData(qr);
                break;
        }

    }

    private void getSpByQr(String qr) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!qr.isEmpty())
                map.put("qrCode", qr);
            getDataManager().doGetSpByQr(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            ServiceProvider serviceProvider = getDataManager().getGson().fromJson(response.body().string(), ServiceProvider.class);
                            if (serviceProvider != null) {
                                getNavigator().onSuccessServiceProviderData(serviceProvider);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data);
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }


    void getEmployeeScannedData(String code) {
        if (getNavigator().isNetworkConnected()) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("type", "expected");
            if (!code.isEmpty())
                map.put("qrCode", code);
            getDataManager().doGetCommercialStaffByQRCode(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            CommercialStaffResponse.ContentBean officeStaffResponse = getDataManager().getGson().fromJson(response.body().string(), CommercialStaffResponse.ContentBean.class);
                            if (officeStaffResponse != null) {
                                getNavigator().onScannedDataRetrieve(officeStaffResponse);
                            } else {
                                getNavigator().showAlert(R.string.alert, R.string.no_data);
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
                    getNavigator().hideLoading();
                    getNavigator().handleApiFailure(t);
                }
            });
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> setClickVisitorDetail(CommercialStaffResponse.ContentBean bean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setCommercialStaff(bean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, bean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_designation, bean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_staff_id, CommonUtils.paritalEncodeData(bean.getEmployeeId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_slot,
                bean.getTimeIn().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeIn(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM),
                bean.getTimeOut().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDateWithOutUTC(bean.getTimeOut(), CalenderUtils.TIME_FORMAT, CalenderUtils.TIME_FORMAT_AM))));
        if (!bean.getEmployment().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.employment, AppUtils.capitaliseFirstLetter(bean.getEmployment()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), bean.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, bean.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("+ ".concat(bean.getDialingCode()).concat(" ").concat(bean.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity_type, bean.getDocumentType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : getIdentityType(bean.getDocumentType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, bean.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData(bean.getDocumentId()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_nationality, bean.getNationality().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getNationality())));
        if (!bean.getPremiseName().isEmpty()) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_dynamic_premise, getDataManager().getLevelName(), bean.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : bean.getPremiseName())));
        }
        if (bean.getMode() != null) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.visitor_mode, bean.getMode())));
        }
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void doCheckIn() {
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("staffId", getDataManager().getCommercialStaff().getId());
                object.put("enteredVehicleNo", getDataManager().getCommercialStaff().getEnteredVehicleNo());
                object.put("bodyTemperature", getDataManager().getCommercialStaff().getBodyTemperature());
                object.put("premiseHierarchyDetailsId", getDataManager().getCommercialStaff().getFlatId());
                object.put("type", AppConstants.CHECK_IN);
            } catch (JSONException e) {
                AppLogger.w("ExpectedCommercialStaffViewModel", e.toString());
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
                            getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), object1.getString("result")).setOnPositiveClickListener(DialogFragment::dismiss);
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
    }

    public void sendPanicAlert(String input) {
        getNavigator().showLoading();
        if (getNavigator().isNetworkConnected(true)) {
            JSONObject object = new JSONObject();
            try {
                object.put("accountId", getDataManager().getAccountId());
                object.put("userMasterId", getDataManager().getUserDetail().getId());
                object.put("description", input);
                object.put("role", "DEVICE_ADMIN");

            } catch (JSONException e) {
                AppLogger.w("ExpectedCommercialStaffViewModel", e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getNavigator().showLoading();
            getDataManager().doPostPanicNotification(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            getNavigator().showAlert(getNavigator().getContext().getString(R.string.success), getNavigator().getContext().getString(R.string.panic_send)).setOnPositiveClickListener(DialogFragment::dismiss);
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
    }

    public void checkinOutSp(ServiceProvider serviceProvider) {
        JSONObject object = new JSONObject();
        try {
            object.put("accountId", getDataManager().getAccountId());
            object.put("documentId", serviceProvider.getIdentityNo());
            object.put("id", serviceProvider.getServiceProviderId());
            object.put("type", serviceProvider.getCheckInTime().isEmpty() ? AppConstants.CHECK_IN : AppConstants.CHECK_OUT);
            object.put("state", AppConstants.ACCEPT);
            object.put("visitor", AppConstants.SERVICE_PROVIDER);

        } catch (JSONException e) {
            AppLogger.w(TAG, e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        doCheckInOut(body, this);

    }
}
