package com.evisitor.data.local.prefs;

import android.app.Activity;

import com.evisitor.data.model.Guests;
import com.evisitor.data.model.UserDetail;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
public interface PreferenceHelper {

    boolean isLoggedIn();

    void setLoggedIn(boolean isLoggedIn);

    boolean isRememberMe();

    void setRememberMe(boolean isRemember);

    String getLanguage();

    void setLanguage(String language);

    String getUsername();

    void setUsername(String userName);

    String getUserPassword();

    void setUserPassword(String userPassword);

    String getAccessToken();

    void setAccessToken(String token);

    String getAccountId();

    void setAccountId(String accId);

    String getUserId();

    void setUserId(String userId);

    UserDetail getUserDetail();

    void setUserDetail(UserDetail userDetail);

    void logout(Activity activity);

    boolean isIdentifyFeature();

    void setIdentifyFeature(boolean identityFeature);

}
