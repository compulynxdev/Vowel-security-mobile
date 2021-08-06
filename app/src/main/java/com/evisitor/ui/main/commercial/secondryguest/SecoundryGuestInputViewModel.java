package com.evisitor.ui.main.commercial.secondryguest;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.SecondaryGuest;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

import java.util.List;

public class SecoundryGuestInputViewModel extends BaseViewModel<BaseNavigator> {

    public SecoundryGuestInputViewModel(DataManager dataManager) {
        super(dataManager);
    }

    boolean verifyGuestDetails(List<SecondaryGuest> beans) {
        for (int i = 0; i < beans.size(); i++) {
            SecondaryGuest bean = beans.get(i);
            if (bean.getFullName().isEmpty()) {
                return false;
            } else if (getDataManager().getGuestConfiguration().getSecGuestField().isSecContactNo() && bean.getContactNo().isEmpty() && !bean.isMinor()) {
                return false;
            } else if (getDataManager().getGuestConfiguration().getSecGuestField().isSecDocumentID() && (bean.getDocumentType().isEmpty() || bean.getDocumentId().isEmpty()) && !bean.isMinor()) {
                return false;
            } else if (getDataManager().getGuestConfiguration().getSecGuestField().isSecAddress() && bean.getAddress().isEmpty() && !bean.isMinor()) {
                return false;
            }
        }
        return true;
    }

}
