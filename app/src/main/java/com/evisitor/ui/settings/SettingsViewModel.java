package com.evisitor.ui.settings;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.EVisitor;
import com.evisitor.data.DataManager;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppUtils;

public class SettingsViewModel extends BaseViewModel<BaseNavigator> {
    private MutableLiveData<String> version = new MutableLiveData<>();

    public SettingsViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public MutableLiveData<String> getVersion() {
        version.setValue(AppUtils.getAppVersionName(EVisitor.getInstance().getApplicationContext()));
        return version;
    }
}
