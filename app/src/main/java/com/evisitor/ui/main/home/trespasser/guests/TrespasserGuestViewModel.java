package com.evisitor.ui.main.home.trespasser.guests;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CalenderUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrespasserGuestViewModel extends BaseViewModel<TrespasserGuestNavigator> {
    public TrespasserGuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void getTrespasserGuest(int page, String search) {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());
            if (!search.isEmpty())
                map.put("search", search);
            map.put("page", "" + page);
            map.put("size", String.valueOf(AppConstants.LIMIT));
            map.put("type", AppConstants.TODAY);
            AppLogger.d("Searching : Trespasser", page + " : " + search);

            getDataManager().doGetAllTrespasserGuest(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    getNavigator().hideLoading();
                    getNavigator().hideSwipeToRefresh();
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            TrespasserResponse registeredHKResponse = getDataManager().getGson().fromJson(response.body().string(), TrespasserResponse.class);
                            if (registeredHKResponse.getContent() != null) {
                                AppLogger.d("Searching : Size", "" + registeredHKResponse.getContent().size());
                                getNavigator().onTrespasserSuccess(registeredHKResponse.getContent());
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
        } else {
            getNavigator().hideSwipeToRefresh();
            getNavigator().hideLoading();
            getNavigator().showAlert(getNavigator().getContext().getString(R.string.alert), getNavigator().getContext().getString(R.string.alert_internet));
        }
    }

    List<VisitorProfileBean> getVisitorDetail(TrespasserResponse.ContentBean visitorResponse) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, visitorResponse.getFullName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getFullName())));

        if (getDataManager().isIdentifyFeature())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_identity, visitorResponse.getDocumentId().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getDocumentId())));

        if (getDataManager().getGuestConfiguration().getGuestField().isGender())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_gender, visitorResponse.getGender().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getGender())));

        if (getDataManager().getGuestConfiguration().getGuestField().isContactNo())
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, visitorResponse.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : "+ ".concat(visitorResponse.getDialingCode()).concat(" ").concat(visitorResponse.getContactNo()))));
        Date hostCheckoutTime = CalenderUtils.getDateFormat(visitorResponse.getHostCheckOutTime(), CalenderUtils.SERVER_DATE_FORMAT);
        if (hostCheckoutTime != null) {
            visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_elapsed, DateUtils.getRelativeTimeSpanString(hostCheckoutTime.getTime()))));
        }
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_time_in, visitorResponse.getCheckInTime().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDate(visitorResponse.getCheckInTime(), CalenderUtils.SERVER_DATE_FORMAT, CalenderUtils.TIMESTAMP_FORMAT))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_house, visitorResponse.getPremiseName().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getPremiseName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_host, visitorResponse.getCreatedBy().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getCreatedBy())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }

}
