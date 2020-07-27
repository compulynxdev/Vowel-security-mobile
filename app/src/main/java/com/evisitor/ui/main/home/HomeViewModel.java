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
    private MutableLiveData<List<HomeBean>> homeListData = new MutableLiveData<>();

    public HomeViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<HomeBean>> getHomeListData() {
        List<HomeBean> list = new ArrayList<>();
        list.add(new HomeBean(R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_guests), "10"));
        list.add(new HomeBean(R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_house_keeping), "50"));
        list.add(new HomeBean(R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_service_provider), "40"));
        list.add(new HomeBean(R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_ttl_expected_visitor), "100"));
        list.add(new HomeBean(R.drawable.ic_person, getNavigator().getContext().getString(R.string.title_blacklisted_visitor), "0"));
        homeListData.setValue(list);
        return homeListData;
    }
}
