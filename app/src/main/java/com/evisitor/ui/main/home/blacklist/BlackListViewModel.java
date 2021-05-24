package com.evisitor.ui.main.home.blacklist;

import androidx.annotation.NonNull;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.BlackListVisitorResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CalenderUtils;
import com.evisitor.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlackListViewModel extends BaseViewModel<BlackListNavigator> {
    public BlackListViewModel(DataManager dataManager) {
        super(dataManager);
    }


    void getData(int page, String search) {
        AppLogger.e("MYPage", page + " : " + search);
        Map<String, String> map = new HashMap<>();
        map.put("accountId", getDataManager().getAccountId());
        if (!search.isEmpty())
            map.put("search", search);
        map.put("page", "" + page);
        map.put("size", String.valueOf(AppConstants.LIMIT));
        map.put("type", AppConstants.BLACK);
        AppLogger.d("Searching : BlackList", "" + page);
        getDataManager().doGetBlackListVisitors(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                getNavigator().hideLoading();
                getNavigator().hideSwipeToRefresh();
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        BlackListVisitorResponse visitorResponse = getDataManager().getGson().fromJson(response.body().string(), BlackListVisitorResponse.class);
                        if (visitorResponse.getContent() != null)
                            getNavigator().OnSuccessBlackList(visitorResponse.getContent());
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

    List<VisitorProfileBean> getVisitorDetail(BlackListVisitorResponse.ContentBean visitorResponse) {
        getNavigator().showLoading();
        List<VisitorProfileBean> visitorProfileBeanList = new ArrayList<>();
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_name, visitorResponse.getFullName())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_profile, visitorResponse.getProfile().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getProfile())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_type, visitorResponse.getType().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.getVisitorType(visitorResponse.getType()))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_mobile, visitorResponse.getContactNo().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CommonUtils.paritalEncodeData("+".concat(visitorResponse.getDialingCode()).concat(" ").concat(visitorResponse.getContactNo())))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_blacklisted_by, visitorResponse.getLastModifiedBy().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getLastModifiedBy())));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_blacklisted_date, visitorResponse.getLastModifiedDate().isEmpty() ? getNavigator().getContext().getString(R.string.na) : CalenderUtils.formatDate(visitorResponse.getLastModifiedDate(), CalenderUtils.SERVER_DATE_FORMAT2, CalenderUtils.CUSTOM_TIMESTAMP_FORMAT_SLASH))));
        visitorProfileBeanList.add(new VisitorProfileBean(getNavigator().getContext().getString(R.string.data_reason, visitorResponse.getReason().isEmpty() ? getNavigator().getContext().getString(R.string.na) : visitorResponse.getReason())));

        getNavigator().hideLoading();
        return visitorProfileBeanList;
    }
}
