package com.evisitor.eVisitor.ui.base;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.StringRes;

import okhttp3.ResponseBody;
/**
 * Created by priyanka joshi
 * Date: 15/07/20
 */

public interface BaseNavigator {

    Context getContext();

    boolean hasPermission(String permission);

    void hideKeyboard();

    boolean isNetworkConnected();

    void requestPermissionsSafely(String[] permissions, int requestCode);

    void hideLoading();

    void showLoading();

   /* AlertDialog showAlert(@StringRes int title, @StringRes int msg);

    AlertDialog showAlert(@StringRes int title, String msg);

    AlertDialog showAlert(String title, @StringRes int msg);
*/
   // AlertDialog showAlert(String title, String msg);

    void showToast(String msg);

    void showToast(@StringRes int msg);
/*
    void handleApiError(ResponseBody response);

    void createLog(String activityName, String action);*/
}
