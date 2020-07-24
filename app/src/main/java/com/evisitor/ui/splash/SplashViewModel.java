package com.evisitor.ui.splash;

import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseViewModel;


/**
 * Created by Hemant Sharma on 15-01-20.
 * Divergent software labs pvt. ltd
 */
public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager) {
        super(dataManager);
    }

    void decideNextActivity() {
        getNavigator().openLoginActivity();
        /*new Handler().postDelayed(() -> {
            if (getDataManager().isLoggedIn()) {
                getNavigator().openMainActivity();
            } else {
                getNavigator().openLoginActivity();
            }
        }, 1500);*/
    }
}
