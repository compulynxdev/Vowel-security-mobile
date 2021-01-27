package com.evisitor.ui.main.home;

import com.evisitor.data.model.ResidentProfile;
import com.evisitor.ui.base.BaseNavigator;

public interface HomeNavigator extends BaseNavigator {
    void onSuccessResidentData(ResidentProfile profile);
}
