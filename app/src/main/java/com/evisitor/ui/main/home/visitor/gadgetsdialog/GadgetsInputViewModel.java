package com.evisitor.ui.main.home.visitor.gadgetsdialog;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.List;

public class GadgetsInputViewModel extends BaseViewModel<BaseNavigator> {

    private MutableLiveData<List<DeviceBean>> deviceBeans = new MutableLiveData<>();

    public GadgetsInputViewModel(DataManager dataManager) {
        super(dataManager);
    }

}
