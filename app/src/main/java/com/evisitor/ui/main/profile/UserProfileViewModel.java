package com.evisitor.ui.main.profile;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.UserDetail;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;

public class UserProfileViewModel extends BaseViewModel<BaseNavigator> {
    private MutableLiveData<UserDetail> userDetailMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> accountName = new MutableLiveData<>();
    public UserProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<UserDetail> getUserDetail() {
        userDetailMutableLiveData.setValue(getDataManager().getUserDetail());
        return userDetailMutableLiveData;
    }

    MutableLiveData<String> getAccountName(){
        accountName.setValue(getDataManager().getAccountName());
        return  accountName;
    }
}
