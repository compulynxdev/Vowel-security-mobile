package com.evisitor.ui.main.home.guest.expected;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.Guests;
import com.evisitor.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class GuestViewModel extends BaseViewModel<GuestNavigator> {
    private MutableLiveData<List<Guests>> guestListData = new MutableLiveData<>();
    public GuestViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<Guests>> getGuestListData(int page, String search) {
        List<Guests> list = new ArrayList<>();
        guestListData.setValue(list);
        return guestListData;
    }
}
