package com.evisitor.util;


/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class AppConstants {

    // common file and image path
    public static final int REQUEST_TAKE_PHOTO = 51;
    public static final int REQUEST_GALLERY = 52;
    public static final int LIMIT = 20;

    public static final String ACTIVITY_ABOUT_US = "ACTIVITY_ABOUT_US";
    public static final String ACTIVITY_PRIVACY = "ACTIVITY_PRIVACY";
    public static final String CHECK_IN = "CHECK_IN";

    public static String CONTENT_TYPE_TEXT = "text/plain";
    public static String CONTENT_TYPE_JSON="application/json; charset=utf-8";
    public static String bearer = "Bearer";

    private AppConstants() {
        // This class is not publicly instantiable
    }
}
