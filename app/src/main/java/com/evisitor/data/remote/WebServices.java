package com.evisitor.data.remote;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
class WebServices {
    public static final String GET_EXPECTED_GUEST_LIST_DETAIL = "guest/get_all_booked_guest";
    static final String BASE_URL = "http://localhost:8003/e-visitor-system/api/";

    static final String LOGIN_AUTH = "authenticate";
    static final String GET_USER_DETAIL = "usermaster/get_user_details"; //?username=ram

    private WebServices() {
    }
}
