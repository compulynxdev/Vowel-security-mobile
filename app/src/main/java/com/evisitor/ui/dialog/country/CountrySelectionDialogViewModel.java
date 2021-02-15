package com.evisitor.ui.dialog.country;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppLogger;
import com.evisitor.util.CommonUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CountrySelectionDialogViewModel extends BaseViewModel<BaseNavigator> {

    private final MutableLiveData<List<CountryResponse>> responseCountryLiveData = new MutableLiveData<>();

    public CountrySelectionDialogViewModel(DataManager dataManager) {
        super(dataManager);
    }

    MutableLiveData<List<CountryResponse>> getResponseCountryLiveData() {
        if (getDataManager().getCountryResponseList() == null) {
            String jsonFileString = CommonUtils.loadJSONFromAsset(getNavigator().getContext(), "country.json");

            if (jsonFileString != null) {
                AppLogger.i("data", jsonFileString);
                Type listType = new TypeToken<List<CountryResponse>>() {
                }.getType();
                List<CountryResponse> countryResponseList = getDataManager().getGson().fromJson(jsonFileString, listType);
                getDataManager().setCountryResponse(countryResponseList);
                responseCountryLiveData.setValue(countryResponseList);
            }
        } else responseCountryLiveData.setValue(getDataManager().getCountryResponseList());
        return responseCountryLiveData;
    }
}
