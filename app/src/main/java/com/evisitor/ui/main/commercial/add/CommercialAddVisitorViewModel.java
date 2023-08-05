package com.evisitor.ui.main.commercial.add;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.AddVisitorData;
import com.evisitor.data.model.CompanyBean;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.ProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommercialAddVisitorViewModel extends BaseViewModel<CommercialAddVisitorNavigator> {
    private final MutableLiveData<Boolean> guestStatusMutableData = new MutableLiveData<>();
    private final List<String> genderList = new ArrayList<>();
    private final List<IdentityBean> identityTypeList = new ArrayList<>();
    private final List<String> visitorTypeList = new ArrayList<>();
    private final MutableLiveData<String> numberPlate = new MutableLiveData<>();
    private final List<String> visitorTypeModeList = new ArrayList<>();
    private final MutableLiveData<List<HouseDetailBean>> houseDetailMutableList = new MutableLiveData<>();
    private final List<String> employmentList = new ArrayList<>();
    private final MutableLiveData<List<ProfileBean>> profileBeanList = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyBean>> companyBeanList = new MutableLiveData<>();

    public CommercialAddVisitorViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getNumberPlate() {
        return numberPlate;
    }

    boolean doVerifyGuestInputs(AddVisitorData visitorData, GuestConfigurationResponse configurationResponse) {
        if (visitorData.name.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_name);
            return false;
        } else if (getDataManager().isIdentifyFeature() && visitorData.identityNo.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_id);
            return false;
        } else if (!visitorData.identityNo.isEmpty() && visitorData.idType.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_id);
            return false;
        } else if (!visitorData.idType.isEmpty() && visitorData.nationality.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_nationality);
            return false;
        } else if (configurationResponse.getGuestField().isContactNo() && visitorData.contact.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_contact);
            return false;
        } else if (configurationResponse.getGuestField().isContactNo() && (visitorData.contact.length() < 7 || visitorData.contact.length() > 12)) {
            getNavigator().showToast(R.string.alert_contact_length);
            return false;
        } else if (configurationResponse.getGuestField().isContactNo() && visitorData.contact.startsWith("0")) {
            getNavigator().showToast(R.string.alert_contact_not_start_with_zero);
            return false;
        } else if (configurationResponse.getGuestField().isGender() && visitorData.gender.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_gender);
            return false;
        } else if (visitorData.houseId.isEmpty()) {
            getNavigator().showToast(R.string.alert_whom_to_meet);
            return false;
        } else if (visitorData.purpose.isEmpty()) {
            getNavigator().showToast(R.string.please_enter_purpose);
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
                object.put("bodyTemperature", addVisitorData.bodyTemperature);
                object.put("email", "");
                object.put("nationality", addVisitorData.nationality);
                object.put("documentType", addVisitorData.identityNo.isEmpty() ? "" : addVisitorData.idType);
                object.put("documentId", addVisitorData.identityNo);
                object.put("dialingCode", addVisitorData.dialingCode);
                object.put("contactNo", addVisitorData.contact);
                object.put("guestType", AppConstants.WALKIN_VISITOR);
                object.put("type", "random");
                object.put("companyName", addVisitorData.visitorCompanyName);
                object.put("mode", addVisitorData.mode);
                object.put("address", addVisitorData.address);
                object.put("country", "");
                object.put("groupType", addVisitorData.groupType);
                object.put("userMasterId", getDataManager().getUserDetail().getId());
                object.put("status", true);
                if (addVisitorData.guestList.size() > 0) {
                    JSONArray guestList = new JSONArray(new Gson().toJson(addVisitorData.guestList));
                    object.put("guestList", guestList);
                }

                if (addVisitorData.isStaffSelect) {
                    object.put("employeeId", addVisitorData.houseId);
                } else {
                    object.put("premiseHierarchyDetailsId", addVisitorData.houseId);  //house or flat id
                }
                object.put("expectedVehicleNo", addVisitorData.vehicleNo.toUpperCase());
                object.put("enteredVehicleNo", addVisitorData.vehicleNo.toUpperCase());
                object.put("vehicleModel", addVisitorData.vehicleModel.toUpperCase());
                object.put("gender", addVisitorData.gender);
                object.put("residentId", addVisitorData.residentId); //host id
                object.put("cardId", "");
                object.put("dob", "");
                if (addVisitorData.bmp_profile == null) {
                    object.put("image", addVisitorData.imageUrl == null ? "" : addVisitorData.imageUrl);
                } else {
                    object.put("image", AppUtils.getBitmapToBase64(addVisitorData.bmp_profile));
                }
                object.put("validateImageUrl", addVisitorData.imageUrl != null && !addVisitorData.imageUrl.isEmpty());
                object.put("state", addVisitorData.isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                object.put("purposeOfVisit", addVisitorData.purpose);
                JSONArray deviceList = new JSONArray(new Gson().toJson(addVisitorData.deviceBeanList));
                object.put("deviceList", deviceList);
                if (!addVisitorData.isAccept) {
                    object.put("rejectedBy", getDataManager().getUserDetail().getFullName());
                    object.put("rejectReason", addVisitorData.rejectedReason);
                }

                if (addVisitorData.vehicalNoPlateBitMapImg != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(addVisitorData.vehicalNoPlateBitMapImg));
                }
            } catch (Exception e) {
                AppLogger.w("CommercialAddVisitorViewModel", e.toString());
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doAddCommercialGuest(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            //AppLogger.e("Response", response.body().string());
                            Guests guests = getDataManager().getGson().fromJson(response.body().string(), Guests.class);
                            getNavigator().onSuccess(addVisitorData.isAccept, guests.isFlagedVisitorStatus());
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

    boolean doVerifySPInputs(AddVisitorData visitorData) {
        if (visitorData.assignedTo.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_assigned_to);
            return false;
        } else if (visitorData.name.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_name);
            return false;
        } else if (visitorData.identityNo.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_id);
            return false;
        } else if (visitorData.idType.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_id);
            return false;
        } else if (visitorData.nationality.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_nationality);
            return false;
        } else if (visitorData.contact.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_contact);
            return false;
        } else if (visitorData.contact.length() < 7 || visitorData.contact.length() > 12) {
            getNavigator().showToast(R.string.alert_contact_length);
            return false;
        } else if (visitorData.contact.startsWith("0")) {
            getNavigator().showToast(R.string.alert_contact_not_start_with_zero);
            return false;
        } else if (visitorData.gender.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_gender);
            return false;
        }/* else if (visitorData.houseId.isEmpty()) {
            getNavigator().showToast(getNavigator().getContext().getString(R.string.please_select).concat(" ").concat(getDataManager().getLevelName()));
            return false;
        }*/ else if (visitorData.employment.isEmpty()) {
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
        } else if (visitorData.mode.isEmpty()) {
            getNavigator().showToast(R.string.please_select_visitor_mode);
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
                object.put("nationality", visitorData.nationality);
                object.put("dialingCode", visitorData.dialingCode);
                object.put("contactNo", visitorData.contact);
                object.put("type", "random");
                object.put("mode", visitorData.mode);
                object.put("address", visitorData.address);
                object.put("country", "");
                object.put("premiseHierarchyDetailsId", visitorData.houseId);  //house or flat id  //->
                object.put("expectedVehicle", visitorData.vehicleNo.toUpperCase());
                object.put("enteredVehicleNo", visitorData.vehicleNo.toUpperCase());
                object.put("expectedVehicleNo", visitorData.vehicleNo.toUpperCase());
                object.put("vehicleModel", visitorData.vehicleModel.toUpperCase());
                object.put("gender", visitorData.gender);
                object.put("bodyTemperature", visitorData.bodyTemperature);
                if (visitorData.bmp_profile == null) {
                    object.put("image", visitorData.imageUrl == null ? "" : visitorData.imageUrl);
                } else {
                    object.put("image", AppUtils.getBitmapToBase64(visitorData.bmp_profile));
                }
                object.put("validateImageUrl", visitorData.imageUrl != null && !visitorData.imageUrl.isEmpty());
                object.put("state", visitorData.isAccept ? AppConstants.ACCEPT : AppConstants.REJECT);
                if (!visitorData.isAccept) {
                    object.put("rejectedBy", getDataManager().getUserDetail().getFullName());
                    object.put("rejectReason", visitorData.rejectedReason);
                }
                if (visitorData.vehicalNoPlateBitMapImg != null) {
                    object.put("vehicleImage", AppUtils.getBitmapToBase64(visitorData.vehicalNoPlateBitMapImg));
                }
            } catch (Exception e) {
                AppLogger.w("CommercialAddVisitorViewModel", e.toString());
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
                            getNavigator().onSuccess(visitorData.isAccept, false);
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
            visitorTypeList.add("Visitor");
            visitorTypeList.add("Service Provider");
        }
        return visitorTypeList;
    }

    List getVisitorTypeMode() {
        if (visitorTypeModeList.isEmpty()) {
            visitorTypeModeList.add("Walk-In");
            visitorTypeModeList.add("Drive-In");
        }
        return visitorTypeModeList;
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

    void doGetHouseDetails() {
        if (getNavigator().isNetworkConnected(true)) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            map.put("search", "");
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

    List doGetHouseDetailsList() {
        return houseDetailMutableList.getValue();
    }

    MutableLiveData<List<HouseDetailBean>> doGetLiveHouseDetails() {
        return houseDetailMutableList;
    }

    void numberPlateVerification(Bitmap bitmap) {
        MultipartBody.Part body = AppUtils.prepareFilePart("upload", "image/png", AppUtils.bitmapToFile(getNavigator().getContext(), bitmap, UUID.randomUUID().toString().concat(".png")));
        getDataManager().doNumberPlateDetails(AppConstants.TOKEN.concat(" ").concat(getDataManager().getUserDetail().getApiKey()), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.code() == 201) {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("results")) {
                            JSONArray array = jsonObject.getJSONArray("results");
                            if (array.length() > 0) {
                                if (array.getJSONObject(0).has("plate")) {
                                    String numerPlate = array.getJSONObject(0).getString("plate");
                                    numberPlate.setValue(numerPlate);
                                }
                            }
                        }

                    } else if (response.code() == 401) {
                        getNavigator().openActivityOnTokenExpire();
                    } else getNavigator().handleApiError(response.errorBody());
                } catch (Exception e) {
                    AppLogger.w("VisitorProfileViewModel", e.toString());
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
