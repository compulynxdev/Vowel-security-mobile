package com.evisitor.ui.main.home;

import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

public interface HomeNavigator extends BaseNavigator {
    void onSuccessResidentData(ResidentProfile profile);

    void onSuccessGuestData(Guests guests);

    void onSuccessServiceProviderData(ServiceProvider serviceProvider);

    void onResponseRecurrentData(RecurrentVisitor recurrentVisitor);

    void onScannedDataRetrieve(CommercialStaffResponse.ContentBean officeStaffResponse);
}
