package com.evisitor.ui.main.home.guest.expected;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
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

public class GuestViewModel extends BaseViewModel<GuestNavigator> {
    private MutableLiveData<List<Guests>> guestListData = new MutableLiveData<>();
    public GuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<Guests>> getGuestListData(int page, String search) {
        getNavigator().showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
        map.put("search", search);
        map.put("page", ""+page);
        map.put("offset", search);
        List<Guests> list = new ArrayList<>();
        getDataManager().doGetExpectedGuestListDetail(getDataManager().getHeader(),map).enqueue(new Callback<GuestsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GuestsResponse> call,@NonNull  Response<GuestsResponse> response) {
                getNavigator().hideLoading();
                if (response.code()==200){
                    assert response.body()!=null;
                    if (response.body().getContent().size()>0){
                        list.addAll(response.body().getContent());
                    }
                }else if (response.code()==401){
                    getNavigator().openActivityOnTokenExpire();
                }
                else getNavigator().handleApiError(response.errorBody());
            }
            @Override
            public void onFailure(@NonNull Call<GuestsResponse> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
        guestListData.setValue(list);
        return guestListData;
    }

    List<VisitorProfileBean> setClickVisitorDetail(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        getDataManager().setGuestDetail(guests);
        visitorProfileBeanList.add(new VisitorProfileBean(guests.getName()));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle),guests.getExpectedVehicleNo(),true));
        if (!guests.getContactNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(guests.getContactNo()));
        if (!guests.getIdentityNo().isEmpty())
            visitorProfileBeanList.add(new VisitorProfileBean(guests.getIdentityNo()));
        visitorProfileBeanList.add(new VisitorProfileBean(guests.getHouseNo()));
        visitorProfileBeanList.add(new VisitorProfileBean(guests.getHost()));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

    void sendNotification() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("guestId",getDataManager().getGuestDetail().getGuestId());
            object.put("accountId",getDataManager().getAccountId());
            object.put("residentId",getDataManager().getGuestDetail().getResidentId());
            object.put("premiseHierarchyDetailsId", getDataManager().getGuestDetail().getFlatId());
        } catch (JSONException e) {
            e.printStackTrace();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON,object.toString());
        getDataManager().doGuestSendNotification(getDataManager().getHeader(),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull  Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200){
                    getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess)
                            ,getNavigator().getContext().getString(R.string.send_notification_success)).setOnPositiveClickListener(dialog -> {
                                dialog.dismiss();
                                getNavigator().refreshList();
                            });
                }else if (response.code()==401){
                    getNavigator().openActivityOnTokenExpire();
                }  else{
                    getNavigator().handleApiError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });

    }

    void approveByCall() {
        getNavigator().showLoading();
        JSONObject object = new JSONObject();
        try {
            object.put("guestId",getDataManager().getGuestDetail().getGuestId());
            object.put("enteredVehicleNo",getDataManager().getGuestDetail().getExpectedVehicleNo());
            object.put("type", AppConstants.CHECK_IN);
        } catch (JSONException e) {
            e.printStackTrace();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),e.toString());
        }

        RequestBody body = AppUtils.createBody(AppConstants.CONTENT_TYPE_JSON,object.toString());
        getDataManager().doGuestCheckInCheckOut(getDataManager().getHeader(),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull  Response<ResponseBody> response) {
                getNavigator().hideLoading();
                if (response.code() == 200){
                    try {
                        assert response.body()!=null;
                        JSONObject object1 = new JSONObject(response.body().string());
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.susscess),object1.getString("result")).setOnPositiveClickListener(dialog -> {
                            dialog.dismiss();
                            getNavigator().refreshList();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),e.toString());
                    }
                }else if (response.code()==401){
                   getNavigator().openActivityOnTokenExpire();
                }  else{
                    getNavigator().handleApiError(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                getNavigator().hideLoading();
                getNavigator().handleApiFailure(t);
            }
        });
    }
}
