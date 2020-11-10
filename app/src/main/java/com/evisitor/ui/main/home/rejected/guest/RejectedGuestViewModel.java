package com.evisitor.ui.main.home.rejected.guest;


import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.GuestsResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RejectedGuestViewModel extends BaseViewModel<RejectedGuestNavigator> {
    public RejectedGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }



    void getGuest(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type","guest");
            AppLogger.d("Searching : Trespasser", page + " : " + search);

            getDataManager().doGetRejectedVisitors(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            GuestsResponse guestResponse = getDataManager().getGson().fromJson(response.body().string(), GuestsResponse.class);
                            if (guestResponse != null) {
                                AppLogger.d("Searching : Size", "" + guestResponse.getContent().size());
                                getNavigator().onSuccess(guestResponse.getContent());
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
        }else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert),getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> getVisitorDetail(Guests guests) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, guests.getName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.vehicle_col), guests.getExpectedVehicleNo(), VisitorProfileBean.VIEW_TYPE_EDITABLE));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, guests.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getDialingCode().concat(guests.getContactNo()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, guests.getIdentityNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getIdentityNo())));
        visitorProfileBeanList.add(new VisitorProfileBean(1, getNavigator().getContext().getString(R.string.data_house, guests.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : guests.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, guests.getHost().isEmpty() ? guests.getCreatedBy() : guests.getHost())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.rejected_by,guests.getRejectedBy().isEmpty() ? getNavigator().getContext().getString(R.string.na) :  guests.getRejectedBy())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_reason,guests.getRejectedReason().isEmpty() ? getNavigator().getContext().getString(R.string.na) :  guests.getRejectedReason())));
        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

}
