package com.evisitor.ui.main.home.sp;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.SPResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppUtils;

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

public class SPViewModel extends BaseViewModel<SPNavigator> {
    private MutableLiveData<List<SPResponse.ContentBean>> spListData = new MutableLiveData<>();

    public SPViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<SPResponse.ContentBean>> getSpListData() {
        return spListData;
    }

    void getSpListData(int page, String search) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));

        getDataManager().doGetExpectedSPList(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        SPResponse spResponse = getDataManager().getGson().fromJson(response.body().string(), SPResponse.class);
                        if (spResponse.getContent() != null) {
                            spListData.setValue(spResponse.getContent());
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

    List<VisitorProfileBean> setClickVisitorDetail(SPResponse.ContentBean spBean) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setSPDetail(spBean);
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, spBean.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, spBean.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), spBean.getExpectedVehicleNo(), true));
        if (!spBean.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, spBean.getContactNo())));
        if (!spBean.getDocumentId().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, spBean.getDocumentId())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, spBean.getFlatNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, spBean.getResidentName())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("guestId", getDataManager().getSpDetail().getId());
            object.put("accountId", getDataManager().getAccountId());

            //Todo check send notification
            /*object.put("residentId", getDataManager().getSpDetail().getResidentId());
            object.put("premiseHierarchyDetailsId", getDataManager().getSpDetail().getFlatId());*/
        } catch (JSONException e) {
            e.printStackTrace();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doGuestSendNotification(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess)
                            , getNavigator().getContext().getString(R.string.send_notification_success)).setOnPositiveClickListener(dialog -> {
                        dialog.dismiss();
                        getNavigator().refreshList();
                    });
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

    void approveByCall() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("id", getDataManager().getSpDetail().getId());
            object.put("enteredVehicleNo", getDataManager().getSpDetail().getExpectedVehicleNo());
            object.put("type", AppConstants.CHECK_IN);
            object.put("visitor", AppConstants.SERVICE_PROVIDER);
        } catch (JSONException e) {
            e.printStackTrace();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON, object.toString());
        getDataManager().doCheckInCheckOut(getDataManager().getHeader(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        JSONObject object1 = new JSONObject(response.body().string());
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess), object1.getString("result")).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();
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
}
