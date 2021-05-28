package com.evisitor.ui.main.commercial.secondryguest;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.data.model.SecoundryGuest;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.List;

public class SecoundryGuestInputViewModel extends BaseViewModel<BaseNavigator> {

    public SecoundryGuestInputViewModel(DataManager dataManager) {
        super(dataManager);
    }

    boolean verifyDeviceDetails(List<SecoundryGuest> beans) {
        for (int i = 0; i < beans.size(); i++) {
            SecoundryGuest bean = beans.get(i);
            if (bean.getFullName().isEmpty()) {
                return false;
            } else if (bean.getContactNo().isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
