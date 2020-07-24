package com.evisitor.data.local.prefs;

import android.app.Activity;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface PreferenceHelper {

    boolean isLoggedIn();

    void setLoggedIn(boolean isLoggedIn);

    void logout(Activity activity);

    String getLanguage();

    void setLanguage(String language);

    String getUsername();

    void setUsername(String userName);

    String getUserPassword();

    void setUserPassword(String userPassword);

    String getAccessToken();

    void setAccessToken(String token);

    String getUserId();

    void setUserId(String userId);

}
