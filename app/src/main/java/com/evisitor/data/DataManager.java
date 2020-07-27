package com.evisitor.data;

import com.evisitor.data.local.prefs.PreferenceHelper;
import com.evisitor.data.remote.ApiHelper;
import com.google.gson.Gson;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface DataManager extends ApiHelper, PreferenceHelper {
    Gson getGson();

    String getHeader();
}
