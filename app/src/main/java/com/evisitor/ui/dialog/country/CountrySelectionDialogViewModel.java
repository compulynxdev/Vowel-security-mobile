package com.evisitor.ui.dialog.country;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.evisitor.data.DataManager;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.util.AppLogger;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CountrySelectionDialogViewModel extends BaseViewModel<BaseNavigator> {

    private MutableLiveData<List<CountryResponse>> responseCountryLiveData = new MutableLiveData<>();

    public CountrySelectionDialogViewModel(DataManager dataManager) {
        super(dataManager);
    }

    private static String getJsonFromAssets(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("CountryCodes.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    MutableLiveData<List<CountryResponse>> getResponseCountryLiveData() {
        if (getDataManager().getCountryResponseList() == null) {
            String jsonFileString = getJsonFromAssets(getNavigator().getContext());
            AppLogger.i("data", jsonFileString);
            Type listType = new TypeToken<List<CountryResponse>>() {
            }.getType();
            List<CountryResponse> countryResponseList = getDataManager().getGson().fromJson(jsonFileString, listType);
            getDataManager().setCountryResponse(countryResponseList);
            responseCountryLiveData.setValue(countryResponseList);
        } else responseCountryLiveData.setValue(getDataManager().getCountryResponseList());
        return responseCountryLiveData;
    }
}
