package com.evisitor.ui.main.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HomeBean;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends BaseViewModel<BaseNavigator> {

    static final int GUEST_VIEW = 0;
    static final int HOUSE_KEEPING_VIEW = 1;
    static final int SERVICE_PROVIDER_VIEW = 2;
    static final int TOTAL_VISITOR_VIEW = 3;
    static final int BLACKLISTED_VISITOR_VIEW = 4;
    static final int TRESPASSER_VIEW = 5;
    static final int FLAGGED_VIEW = 6;
    private MutableLiveData<List<HomeBean>> homeListData = new MutableLiveData<>();
    private MutableLiveData<Integer> notificationCountData = new MutableLiveData<>();
    private List<HomeBean> list = new ArrayList<>();

    public HomeViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HomeBean>> getHomeListData() {
        return homeListData;
    }

    MutableLiveData<Integer> getNotificationCountData() {
        return notificationCountData;
    }

    void setupHomeList() {
        list.add(new HomeBean(GUEST_VIEW, R.drawable.ic_guest, getNavigator().getContext().getString(R.string.title_guests)));
        list.add(new HomeBean(HOUSE_KEEPING_VIEW, R.drawable.ic_maid, getNavigator().getContext().getString(R.string.title_domestic_staff)));
        list.add(new HomeBean(SERVICE_PROVIDER_VIEW, R.drawable.ic_assistance, getNavigator().getContext().getString(R.string.title_service_provider)));
        list.add(new HomeBean(TOTAL_VISITOR_VIEW, R.drawable.ic_group, getNavigator().getContext().getString(R.string.title_ttl_expected_visitor)));
        list.add(new HomeBean(BLACKLISTED_VISITOR_VIEW, R.drawable.ic_black_visitor, getNavigator().getContext().getString(R.string.title_blacklisted_visitor)));
        list.add(new HomeBean(TRESPASSER_VIEW, R.drawable.ic_trespasser, getNavigator().getContext().getString(R.string.title_trespasser_visitor)));
        list.add(new HomeBean(FLAGGED_VIEW, R.drawable.ic_flag_visitor, getNavigator().getContext().getString(R.string.title_flagged_visitor)));
        homeListData.setValue(list);
    }

    void getVisitorCount() {
        if (getNavigator().isNetworkConnected()) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", getDataManager().getAccountId());

            getDataManager().doGetVisitorCount(getDataManager().getHeader(), map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (!list.isEmpty()) {
                                if (jsonObject.has("notification")) {
                                    int notificationCount = jsonObject.getInt("notification");
                                    notificationCountData.setValue(notificationCount);
                                }
                                int guestCount = jsonObject.getInt("guest");
                                int hkCount = jsonObject.getInt("staff");
                                int spCount = jsonObject.getInt("serviceProvider");
                                int totalCount = guestCount + hkCount + spCount;
                                list.get(GUEST_VIEW).setCount(String.valueOf(guestCount));
                                list.get(HOUSE_KEEPING_VIEW).setCount(String.valueOf(hkCount));
                                list.get(SERVICE_PROVIDER_VIEW).setCount(String.valueOf(spCount));
                                list.get(TOTAL_VISITOR_VIEW).setCount(String.valueOf(totalCount));
                                homeListData.setValue(list);
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
                    getNavigator().handleApiFailure(t);
                }
            });
        }
    }

}
