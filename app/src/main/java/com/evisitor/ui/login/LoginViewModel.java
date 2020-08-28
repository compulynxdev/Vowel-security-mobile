package com.evisitor.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.UserDetail;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
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

    private MutableLiveData<String> version = new MutableLiveData<>();

    public LoginViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }

    void doVerifyAndLogin(String userName, String password, boolean isRemember) {
        if (getNavigator().isNetworkConnected()) {
            if (verifyInput(userName, password)) {
                doLogin(userName, password, isRemember);
            }
        } else {
            getNavigator().showAlert(R.string.alert, R.string.alert_internet);
        }
    }

    private boolean verifyInput(String userName, String password) {
        if (userName.isEmpty()) {
            getNavigator().showToast(R.string.alert_empty_username);
            return false;
        } else if (userName.length() < 5 || userName.length() > 10) {
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


    private void doLogin(String userName, String password, boolean isRemember) {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("username", userName);
            object.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doLogin(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object = new JSONObject(response.body().string());
                        getDataManager().setUsername(userName);
                        getDataManager().setUserPassword(password);
                        getDataManager().setAccessToken(object.getString("id_token"));
                        getDataManager().setAccountId(object.getString("accountId"));
                        getDataManager().setIdentifyFeature(object.getBoolean("identityFeature"));
                        getDataManager().setUserId(object.getString("userId"));
                        doGetUserDetail(isRemember);
                    } catch (Exception e) {
                        getNavigator().hideLoading();
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
