package com.evisitor.ui.main.home.customCamera;

import com.evisitor.data.model.MrzResponse;

public interface MRZCallback {
    void onMrzSuccess(MrzResponse response);
    void OnError(String string);
}
