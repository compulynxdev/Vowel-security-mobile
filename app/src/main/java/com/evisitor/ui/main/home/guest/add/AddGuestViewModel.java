package com.evisitor.ui.main.home.guest.add;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
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

public class AddGuestViewModel extends BaseViewModel<AddGuestNavigator> {

    private MutableLiveData<List<HouseDetailBean>> houseDetailMutableList = new MutableLiveData<>();
    private MutableLiveData<List<HostDetailBean>> hostDetailMutableList = new MutableLiveData<>();
    private List<String> genderList = new ArrayList<>();
    private List<IdentityBean> identityTypeList = new ArrayList<>();

    public AddGuestViewModel(DataManager dataManager) {
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

    boolean doVerifyInputs(String identityNo, String idType, String name, String contact, String address, String gender, String houseId, String ownerId, String residentId) {
        if (getDataManager().isIdentifyFeature() && identityNo.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_id);
            return false;
        } else if (!identityNo.isEmpty() && idType.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_id);
            return false;
        } else if (name.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_name);
            return false;
        } else if (contact.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_contact);
            return false;
        } else if (contact.length() < 7 || contact.length() > 12) {
            getNavigator().showToast(R.string.alert_contact_length);
            return false;
        } else if (address.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_address);
            return false;
        } else if (gender.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_gender);
            return false;
        } else if (houseId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_house_no);
            return false;
        } else if (ownerId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_owner);
            return false;
        } else if (residentId.isEmpty()) {
            getNavigator().showToast(R.string.alert_select_host);
            return false;
        } else return true;
    }

    void doAddGuest(Bitmap bmp_profile, String identityNo, String idType, String name, String vehicleNo, String contact, String address, String gender, String houseNumber, String houseId, String ownerId, String residentId) {

        if (getNavigator().isNetworkConnected(true)) {
            getNavigator().showLoading();

            JSONObject object = new JSONObject();
            try {
                object.put("fullName", name);
                object.put("accountId", getDataManager().getAccountId());
                object.put("email", "");
                object.put("documentType", identityNo.isEmpty() ? "" : idType);
                object.put("documentId", identityNo);
                object.put("contactNo", contact);
                object.put("guestType", "RANDOM_VISITOR");
                object.put("address", address);
                object.put("country", "");
                object.put("premiseHierarchyDetailsId", houseId);  //house or flat id
                object.put("expectedVehicleNo", vehicleNo);
                object.put("enteredVehicleNo", vehicleNo);
                object.put("gender", gender);
                object.put("residentId", residentId); //host id
                object.put("cardId", "");
                object.put("dob", "");
                object.put("image", bmp_profile == null ? "" : AppUtils.getBitmapToBase64(bmp_profile));
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doAddGuest(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            AppLogger.e("Response", response.body().string());
                            getNavigator().onSuccess();
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
        }
        return genderList;
    }

    List getIdentityTypeList() {
        if (identityTypeList.isEmpty()) {
            identityTypeList.add(new IdentityBean("National ID", "nationalId"));
            identityTypeList.add(new IdentityBean("Passport", "passport"));
            identityTypeList.add(new IdentityBean("Driving Licence", "dl"));
        }
        return identityTypeList;
    }
}
