package com.evisitor.ui.main.home.customCamera;

import com.evisitor.data.model.MrzResponse;
import com.evisitor.ui.base.BaseNavigator;

import okhttp3.ResponseBody;

public interface CameraActivityNavigator extends BaseNavigator {
    void onMrzSuccess(MrzResponse response);
    void OnError(ResponseBody string);
}

