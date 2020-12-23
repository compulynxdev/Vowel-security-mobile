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

    boolean verifyDeviceDetails(List<DeviceBean> beans) {
        for (int i = 0; i < beans.size(); i++) {
            DeviceBean bean = beans.get(i);
            if (bean.getDeviceName().isEmpty()) {
                return false;
            } else if (bean.getSerialNo().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
