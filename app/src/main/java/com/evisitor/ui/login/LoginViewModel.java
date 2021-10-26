package com.evisitor.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.UserDetail;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private final MutableLiveData<String> version = new MutableLiveData<>();

    public LoginViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }

    void doVerifyAndLogin(String userName, String password, String token, boolean isRemember) {
        if (getNavigator().isNetworkConnected(true)) {
            if (verifyInput(userName, password)) {
                doLogin(userName, password, token, isRemember);
            }
        }
    }

    private boolean verifyInput(String userName, String password) {
        if (userName.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_username);
            return false;
        } else if (userName.length() < 5) {
            getNavigator().showToast(R.string.alert_username_length);
            return false;
        } else if (password.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_password);
            return false;
        } else if (password.length() < 5) {
            getNavigator().showToast(R.string.alert_pwd_Length);
            return false;
        } else return true;
    }


    private void doLogin(String userName, String password, String token, boolean isRemember) {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("username", userName);
            object.put("password", password);
            object.put("fcmToken", token);
        } catch (Exception e) {
            AppLogger.w("doLogin", e.toString());
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doLogin(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object = new JSONObject(response.body().string());
                        if(object.has("mobileMode") && !object.getString("mobileMode").equalsIgnoreCase("webportal")) {
                            if (object.has("role") && object.getString("role").equals("DEVICE_ADMIN")) {
                                getDataManager().setUsername(userName);
                                getDataManager().setUserPassword(password);
                                getDataManager().setAccessToken(object.getString("id_token"));
                                getDataManager().setAccountId(object.getString("accountId"));
                                getDataManager().setIdentifyFeature(object.getBoolean("identityFeature"));
                                getDataManager().setLevelName(object.getString("levelName"));
                                getDataManager().setAccountName(object.getString("accountName"));
                                getDataManager().setCommercial(object.has("type") && object.getString("type").equalsIgnoreCase("COMMERCIAL"));
                                getDataManager().setUserId(object.getString("userId"));
                                if (object.has("printLabel"))
                                    getDataManager().setPrintLabel(object.getBoolean("printLabel"));
                                getDataManager().setCheckOutFeature(object.getBoolean("guestCheckOut"));
                                if (object.has("country"))
                                    getDataManager().setPropertyCountry(object.getString("country"));
                                else
                                    getDataManager().setPropertyCountry(getNavigator().getContext().getString(R.string.default_country));
                                if (object.has("dialingCode"))
                                    getDataManager().setPropertyDialingCode(object.getString("dialingCode"));
                                else
                                    getDataManager().setPropertyDialingCode(getNavigator().getContext().getString(R.string.default_dialing_code));
                                doGetUserDetail(isRemember);
                            } else {
                                getNavigator().hideLoading();
                                getNavigator().showAlert(R.string.alert, R.string.alert_invalid_role);
                            }
                        }else {
                            getNavigator().hideLoading();
                            getNavigator().showAlert(R.string.alert, R.string.web_mode_only);
                        }
                    } catch (Exception e) {
                        getNavigator().hideLoading();
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                } else if (response.code() == 401) {
                    getNavigator().hideLoading();
                    try {
                        assert response.errorBody() != null;
                        JSONObject data = new JSONObject(response.errorBody().string());
                        if (data.has("detail"))
                            getNavigator().showAlert(R.string.alert, R.string.invalid_credentials);
                        else
                            getNavigator().showAlert(R.string.alert, data.getString("respMessage"));
                    } catch (Exception e) {
                        getNavigator().showAlert(R.string.alert, R.string.alert_error);
                    }
                } else {
                    getNavigator().hideLoading();
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

    private void doGetUserDetail(boolean isRemember) {
        Map<String, String> map = new HashMap<>();
        map.put("username", getDataManager().getUsername());
        getDataManager().doGetUserDetail(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        UserDetail userDetail = getDataManager().getGson().fromJson(response.body().string(), UserDetail.class);
                        getDataManager().setUserDetail(userDetail);
                        getDataManager().setRememberMe(isRemember);
                        getDataManager().setLoggedIn(true);
                        getNavigator().openMainActivity();
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
