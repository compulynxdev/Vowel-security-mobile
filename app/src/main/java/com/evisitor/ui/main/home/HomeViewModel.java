package com.evisitor.ui.main.home;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.R;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.HomeBean;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends BaseViewModel<BaseNavigator> {

    static final int GUEST_VIEW = 1;
    static final int HOUSE_KEEPING_VIEW = 2;
    static final int SERVICE_PROVIDER_VIEW = 3;
    static final int TOTAL_VISITOR_VIEW = 4;
    static final int BLACKLISTED_VISITOR_VIEW = 5;
    static final int TRESPASSER_VIEW = 6;
    private MutableLiveData<List<HomeBean>> homeListData = new MutableLiveData<>();

    public HomeViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HomeBean>> getHomeListData() {
        List<HomeBean> list = new ArrayList<>();
        list.add(new HomeBean(GUEST_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_guests), "10"));
        list.add(new HomeBean(HOUSE_KEEPING_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_house_keeping), "50"));
        list.add(new HomeBean(SERVICE_PROVIDER_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_service_provider), "40"));
        list.add(new HomeBean(TOTAL_VISITOR_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_ttl_expected_visitor), "100"));
        list.add(new HomeBean(BLACKLISTED_VISITOR_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_blacklisted_visitor), "0"));
        list.add(new HomeBean(TRESPASSER_VIEW, R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_trespasser_visitor), "0"));
        homeListData.setValue(list);
        return homeListData;
    }
}
