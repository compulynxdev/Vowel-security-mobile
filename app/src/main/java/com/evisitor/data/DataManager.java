package com.evisitor.data;

import com.evisitor.data.local.prefs.PreferenceHelper;
import com.evisitor.data.model.CountryResponse;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.remote.ApiHelper;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface DataManager extends ApiHelper, PreferenceHelper {
    Gson getGson();

    String getHeader();

    void setGuestDetail(Guests guest) ;

     Guests getGuestDetail() ;

    void setSPDetail(ServiceProvider spDetail);

    ServiceProvider getSpDetail();

    HouseKeepingResponse.ContentBean getHouseKeeping();

    void setHouseKeeping(HouseKeepingResponse.ContentBean houseKeeping);

    String getImageBaseURL();

    List<CountryResponse> getCountryResponseList();

    void setCountryResponse(List<CountryResponse> countryResponseList);

    GuestConfigurationResponse getGuestConfiguration();

    void setGuestConfiguration(GuestConfigurationResponse configurationResponse);
}
