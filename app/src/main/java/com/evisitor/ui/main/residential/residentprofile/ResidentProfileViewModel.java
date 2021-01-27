package com.evisitor.ui.main.residential.residentprofile;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.ui.base.BaseViewModel;

public class ResidentProfileViewModel extends BaseViewModel<ResidentProfileNavigator> {
    private MutableLiveData<ResidentProfile> profileLiveData = new MutableLiveData<>();

    public ResidentProfileViewModel(DataManager dataManager) {
        super(dataManager);
    }


    void setProfileLiveData(ResidentProfile profile) {
        profileLiveData.setValue(profile);
    }

    public MutableLiveData<ResidentProfile> getProfileLiveData() {
        return profileLiveData;
    }

}
