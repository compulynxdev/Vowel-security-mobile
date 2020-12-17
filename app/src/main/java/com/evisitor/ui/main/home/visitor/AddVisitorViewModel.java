package com.evisitor.ui.main.home.visitor;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.AddVisitorData;
import com.evisitor.data.model.CompanyBean;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.ProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVisitorViewModel extends BaseViewModel<AddVisitorNavigator> {

    private MutableLiveData<List<HouseDetailBean>> houseDetailMutableList = new MutableLiveData<>();
    private MutableLiveData<List<HostDetailBean>> hostDetailMutableList = new MutableLiveData<>();
    private MutableLiveData<Boolean> guestStatusMutableData = new MutableLiveData<>();
    private List<String> genderList = new ArrayList<>();
    private List<IdentityBean> identityTypeList = new ArrayList<>();
    private List<String> visitorTypeList = new ArrayList<>();
    private List<String> assignedToList = new ArrayList<>();
    private List<String> employmentList = new ArrayList<>();
    private MutableLiveData<List<ProfileBean>> profileBeanList = new MutableLiveData<>();
    private MutableLiveData<List<CompanyBean>> companyBeanList = new MutableLiveData<>();

    public AddVisitorViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HouseDetailBean>> doGetHouseDetails() {
        return houseDetailMutableList;
    }

    void doGetHouseDetails(String search) {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("search", search);
            getDataManager().doGetHouseDetailList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<HouseDetailBean>>() {
                            }.getType();
                            List<HouseDetailBean> houseDetailList = getDataManager().getGson().fromJson(response.body().string(), listType);
                            houseDetailMutableList.setValue(houseDetailList);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    MutableLiveData<List<HostDetailBean>> doGetHostDetails() {
        return hostDetailMutableList;
    }

    void doGetHostDetails(String houseId) {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("flatId", houseId);
            getDataManager().doGetHostDetailList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<HostDetailBean>>() {
                            }.getType();
                            List<HostDetailBean> hostDetailList = getDataManager().getGson().fromJson(response.body().string(), listType);
                            hostDetailMutableList.setValue(hostDetailList);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    boolean doVerifyGuestInputs(AddVisitorData visitorData, GuestConfigurationResponse configurationResponse) {
        if (getDataManager().isIdentifyFeature() && visitorData.identityNo.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_id);
            return false;
        } else if (!visitorData.identityNo.isEmpty() && visitorData.idType.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_id);
            return false;
        } else if (visitorData.name.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_name);
            return false;
        } else if (configurationResponse.getGuestField().isContactNo() && visitorData.contact.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_contact);
            return false;
        } else if (configurationResponse.getGuestField().isContactNo() && (visitorData.contact.length() < 7 || visitorData.contact.length() > 12)) {
            getNavigator().showToast(R.string.alert_contact_length);
            return false;
        }/* else if (configurationResponse.getGuestField().isAddress() && visitorData.address.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_address);
            return false;
        } */ else if (configurationResponse.getGuestField().isGender() && visitorData.gender.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_gender);
            return false;
        } else if (visitorData.houseId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_house_no);
            return false;
        } else if (visitorData.residentId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_host);
            return false;
        } else return true;
    }

    void doAddGuest(AddVisitorData addVisitorData) {

        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();

            JSONObject object = new JSONObject();
            try {
                object.put("fullName", addVisitorData.name);
                object.put("accountId", getDataManager().getAccountId());
                object.put("email", "");
                object.put("documentType", addVisitorData.identityNo.isEmpty() ? "" : addVisitorData.idType);
                object.put("documentId", addVisitorData.identityNo);
                object.put("dialingCode", addVisitorData.dialingCode);
                object.put("contactNo", addVisitorData.contact);
                object.put("guestType", "RANDOM_VISITOR");
                object.put("type", "random");
                object.put("address", addVisitorData.address);
                object.put("country", "");
                object.put("premiseHierarchyDetailsId", addVisitorData.houseId);  //house or flat id
                object.put("expectedVehicleNo", addVisitorData.vehicleNo.toUpperCase());
                object.put("enteredVehicleNo", addVisitorData.vehicleNo.toUpperCase());
                object.put("gender", addVisitorData.gender);
                object.put("residentId", addVisitorData.residentId); //host id
                object.put("cardId", "");
                object.put("dob", "");
                object.put("image", addVisitorData.bmp_profile == null ? "" : AppUtils.getBitmapToBase64(addVisitorData.bmp_profile));
                object.put("state", addVisitorData.isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                if (!addVisitorData.isAccept) {
                    object.put("rejectedBy", getDataManager().getUserDetail().getFullName());
                    object.put("rejectReason", addVisitorData.rejectedReason);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //AppLogger.e("What : Guest", object.toString());
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doAddGuest(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            AppLogger.e("Response", response.body().string());
                            getNavigator().onSuccess(addVisitorData.isAccept);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    boolean doVerifySPInputs(AddVisitorData visitorData) {
        if (visitorData.assignedTo.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_assigned_to);
            return false;
        } else if (visitorData.identityNo.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_id);
            return false;
        } else if (visitorData.idType.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_id);
            return false;
        } else if (visitorData.name.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_name);
            return false;
        } else if (visitorData.contact.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_contact);
            return false;
        } else if (visitorData.contact.length() < 7 || visitorData.contact.length() > 12) {
            getNavigator().showToast(R.string.alert_contact_length);
            return false;
        }/* else if (visitorData.address.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_address);
            return false;
        } */ else if (visitorData.gender.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_gender);
            return false;
        } else if (visitorData.isResident && visitorData.houseId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_house_no);
            return false;
        } else if (visitorData.isResident && visitorData.residentId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_host);
            return false;
        } else if (visitorData.employment.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_employment);
            return false;
        } else if (visitorData.profile.isEmpty()) {
            getNavigator().showToast(R.string.alert_enter_profile);
            return false;
        } else if (visitorData.isCompany && visitorData.companyName.isEmpty()) {
            getNavigator().showToast(R.string.alert_enter_comp_name);
            return false;
        } else if (visitorData.isCompany && visitorData.companyAddress.isEmpty()) {
            getNavigator().showToast(R.string.alert_enter_comp_address);
            return false;
        } else return true;
    }

    void doAddSp(AddVisitorData visitorData) {

        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();

            JSONObject object = new JSONObject();
            try {
                object.put("visitorType", visitorData.visitorType);
                object.put("isVip", false);
                object.put("employment", visitorData.employment);
                object.put("profile", visitorData.profile);
                object.put("companyName", visitorData.companyName);
                object.put("companyAddress", visitorData.companyAddress);

                object.put("fullName", visitorData.name);
                object.put("accountId", getDataManager().getAccountId());
                object.put("email", "");
                object.put("documentType", visitorData.identityNo.isEmpty() ? "" : visitorData.idType);
                object.put("documentId", visitorData.identityNo);
                object.put("dialingCode", visitorData.dialingCode);
                object.put("contactNo", visitorData.contact);
                object.put("type", "random");
                object.put("address", visitorData.address);
                object.put("country", "");
                //object.put("expectedDate", new Date());
                object.put("premiseHierarchyDetailsId", visitorData.isResident ? visitorData.houseId : null);  //house or flat id  //->
                object.put("expectedVehicle", visitorData.vehicleNo.toUpperCase());
                object.put("enteredVehicleNo", visitorData.vehicleNo.toUpperCase());
                object.put("gender", visitorData.gender);
                object.put("residentId", visitorData.isResident ? visitorData.residentId : null); //host id   //-> send null in case of property
                object.put("image", visitorData.bmp_profile == null ? "" : AppUtils.getBitmapToBase64(visitorData.bmp_profile));
                object.put("state", visitorData.isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                if (!visitorData.isAccept) {
                    object.put("rejectedBy", getDataManager().getUserDetail().getFullName());
                    object.put("rejectReason", visitorData.rejectedReason);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //AppLogger.e("What : Sp", object.toString());

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doAddSP(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            AppLogger.e("Response", response.body().string());
                            getNavigator().onSuccess(visitorData.isAccept);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    MutableLiveData<Boolean> getGuestStatusMutableData() {
        return guestStatusMutableData;
    }

    void doCheckGuestStatus(String identity, String docType) {
        if (!getDataManager().isIdentifyFeature()) {
            guestStatusMutableData.setValue(false);
            return;
        }

        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("documentId", identity);
            map.put("documentType", docType);
            getDataManager().doCheckGuestStatus(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("result"))
                                guestStatusMutableData.setValue(jsonObject.getBoolean("result"));
                            else getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    List<String> getGenderList() {
        if (genderList.isEmpty()) {
            genderList.add("Male");
            genderList.add("Female");
            genderList.add("Transgender");
            genderList.add("Prefer not to disclose");
        }
        return genderList;
    }

    List getIdentityTypeList() {
        if (identityTypeList.isEmpty()) {
            identityTypeList.add(new IdentityBean("National ID", "nationalId"));
            identityTypeList.add(new IdentityBean("Driving Licence", "dl"));
            identityTypeList.add(new IdentityBean("Passport", "passport"));
        }
        return identityTypeList;
    }

    List getVisitorTypeList() {
        if (visitorTypeList.isEmpty()) {
            visitorTypeList.add("Guest");
            visitorTypeList.add("Service Provider");
        }
        return visitorTypeList;
    }

    List getAssignedToList() {
        if (assignedToList.isEmpty()) {
            assignedToList.add("Property");
            assignedToList.add("Resident");
        }
        return assignedToList;
    }

    List getEmploymentTypeList() {
        if (employmentList.isEmpty()) {
            employmentList.add("Self");
            employmentList.add("Other");
        }
        return employmentList;
    }

    MutableLiveData<List<ProfileBean>> getProfileSuggestions() {
        return profileBeanList;
    }

    void doGetProfileSuggestions(String search) {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("search", search);
            getDataManager().doGetProfileSuggestions(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<ProfileBean>>() {
                            }.getType();
                            List<ProfileBean> profileBeans = getDataManager().getGson().fromJson(response.body().string(), listType);
                            profileBeanList.setValue(profileBeans);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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

    MutableLiveData<List<CompanyBean>> getCompanySuggestions() {
        return companyBeanList;
    }

    void doGetCompanySuggestions(String search) {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("search", search);
            getDataManager().doGetCompanySuggestions(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            Type listType = new TypeToken<List<CompanyBean>>() {
                            }.getType();
                            List<CompanyBean> companyBeans = getDataManager().getGson().fromJson(response.body().string(), listType);
                            companyBeanList.setValue(companyBeans);
                        } catch (Exception e) {
                            getNavigator().showAlert(R.string.alert, R.string.alert_error);
                        }
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
}
