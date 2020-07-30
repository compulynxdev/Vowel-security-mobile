package com.evisitor.data.remote;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
class WebServices {


    //static final String BASE_URL = "http://localhost:8003/e-visitor-system/api/"; //103
    static final String BASE_URL = "http://localhost:8003/api/"; //local

    static final String LOGIN_AUTH = "authenticate";
    static final String GET_USER_DETAIL = "usermaster/get_user_details"; //?username=ram
    static final String ADD_GUEST = "guest/register_guest";
    static final String GET_HOUSE_LIST = "premise/get_all_flat_list"; //?accountId=1&search=a
    static final String GET_HOST_LIST = "resident/get_resident_by_flat_id"; //?accountId=1&flatId=6

    private WebServices() {
    }
}
