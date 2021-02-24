package com.evisitor.ui.login.forgotpassword;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
import com.evisitor.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends BaseViewModel<ForgotPasswordNavigator> {
    public ForgotPasswordViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void verifyData(String userName, String email, String type) {
        if (userName.isEmpty()) {
            getNavigator().showAlert(R.string.alert, R.string.please_enter_username);
        } else if (type.equalsIgnoreCase("reset") && email.isEmpty()) {
            getNavigator().showAlert(R.string.alert, R.string.please_enter_email);
        } else if (type.equalsIgnoreCase("reset") && !CommonUtils.isEmailValid(email)) {
            getNavigator().showAlert(R.string.alert, R.string.please_enter_email);
        } else {
            getNavigator().showLoading();
            JSONObject object = new JSONObject();
            try {
                object.put("username", userName);
                object.put("email", email);
                object.put("type", type);
            } catch (JSONException e) {
                AppLogger.w("ForgotPasswordViewModel", e.toString());
            }
            RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
            getDataManager().doPasswordReset(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        getNavigator().hideLoading();
                        assert response.body() != null;
                        try {
                            JSONObject object1 = new JSONObject(response.body().string());
                            if (object1.has("result")) {
                                getNavigator().showAlert(R.string.alert, object1.getString("result")).setOnPositiveClickListener(dialog -> {
                                    dialog.dismiss();
                                    getNavigator().openNextActivity();
                                });
                            }
                        } catch (Exception e) {
                            AppLogger.w("ForgotPasswordViewModel", e.toString());
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
    }
}
