package com.evisitor.ui.login;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.EVisitor;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppUtils;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private MutableLiveData<String> version = new MutableLiveData<>();

    public LoginViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }

    /*void doLogin(String userName, String password, boolean isChecked) {
        if (verifyInput(userName, password)) {

        }
    }

    private boolean verifyInput(String userName, String password) {
        if (userName.isEmpty()) {
            getNavigator().showToast(R.string.PleaseEnterUsername);
            return false;
        } else if (userName.length() < 5 || userName.length() > 10) {
            getNavigator().showToast(R.string.UsernameLength);
            return false;
        } else if (password.isEmpty()) {
            getNavigator().showToast(R.string.PleaseEnterPassword);
            return false;
        } else if (password.length() < 5) {
            getNavigator().showToast(R.string.PwdLength);
            return false;
        } else return true;
    }


    private void doLogin() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("username", getDataManager().getUsername());
            object.put("password", getDataManager().getUserPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().getAccessToken(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object = new JSONObject(response.body().string());
                        getDataManager().setAccessToken(object.getString("id_token"));
                        getDataManager().setSchoolId(object.getString("schoolId"));
                        getDataManager().setSchoolName(object.getString("schoolName"));
                        getDataManager().setUserId(object.getString("userId"));
                        getUsers();
                    } catch (Exception e) {
                        getNavigator().hideLoading();
                        getNavigator().showAlert(R.string.alert, e.toString());
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
    }*/
}
