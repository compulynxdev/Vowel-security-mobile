package com.evisitor.ui.main.home.recurrentvisitor;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.AppUtils;
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

public class FilterRecurrentVisitorViewModel extends BaseViewModel<FilterRecurrentVisitorNavigator> {

    private final List<IdentityBean> identityTypeList = new ArrayList<>();
    private final List<String> visitorTypeList = new ArrayList<>();
    private final List<String> filterTypeList = new ArrayList<>();

    public FilterRecurrentVisitorViewModel(DataManager dataManager) {
        super(dataManager);
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
            visitorTypeList.add(getDataManager().isCommercial() ? "Visitor" : "Guest");
            visitorTypeList.add("Service Provider");
        }
        return visitorTypeList;
    }

    List getFilterTypeList() {
        if (filterTypeList.isEmpty()) {
            filterTypeList.add("Identity");
            filterTypeList.add("Name & Contact Number");
        }
        return filterTypeList;
    }

    void getFilteredVisitorData(Boolean isGuest, Boolean isFilterByID, String docId, String docType, String name, String dialingCode, String contactNum) {
        if (getNavigator().isNetworkConnected()) {
            if (validateInputs(isGuest, isFilterByID, docId, docType, name, contactNum)) {

                JSONObject object = new JSONObject();
                try {
                    object.put("accountId", getDataManager().getAccountId());
                    object.put("visitorType", isGuest ? AppConstants.GUEST : AppConstants.SERVICE_PROVIDER);
                    object.put("documentId", docId);
                    object.put("documentType", docType);
                    object.put("name", name);
                    object.put("dialingCode", dialingCode);
                    object.put("contactNo", contactNum);
                } catch (JSONException e) {
                    AppLogger.w("FilterRecurrentVisitorViewModel", e.toString());
                }
                RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());

                getDataManager().doGetFilterVisitorInfo(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        getNavigator().hideLoading();
                        try {
                            if (response.code() == 200) {
                                assert response.body() != null;
                                RecurrentVisitor recurrentVisitor = getDataManager().getGson().fromJson(response.body().string(), RecurrentVisitor.class);
                                recurrentVisitor.setVisitorType(isGuest ? AppConstants.GUEST : AppConstants.SERVICE_PROVIDER);
                                getNavigator().onResponseSuccess(recurrentVisitor);
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
        } else {
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    private boolean validateInputs(Boolean isGuest, Boolean isFilterByID, String docId, String docType, String name, String contactNum) {
        if (isGuest == null) {
            getNavigator().showToast(R.string.alert_select_visitor);
            return false;
        }
        if (isFilterByID == null) {
            getNavigator().showToast(R.string.alert_select_filter);
            return false;
        } else {
            if (isFilterByID) {
                if (docId.isEmpty()) {
                    getNavigator().showToast(R.string.alert_empty_id);
                    return false;
                } else if (docType.isEmpty()) {
                    getNavigator().showToast(R.string.alert_select_id);
                    return false;
                } else return true;
            } else {
                if (name.isEmpty()) {
                    getNavigator().showToast(R.string.alert_empty_name);
                    return false;
                } else if (contactNum.isEmpty()) {
                    getNavigator().showToast(R.string.alert_empty_contact);
                    return false;
                } else if ((contactNum.length() < 7 || contactNum.length() > 12)) {
                    getNavigator().showToast(R.string.alert_contact_length);
                    return false;
                } else return true;
            }
        }
    }
}
