package com.evisitor.ui.dialog;

import android.graphics.Bitmap;

/**
 * Created by hemant.
 * Date: 30/8/18
 * Time: 6:17 PM
 */

public interface ImagePickCallback {
    void onImageReceived(Bitmap bitmap);

    void onView();
}
