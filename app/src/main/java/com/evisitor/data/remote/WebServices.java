package com.evisitor.data.remote;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
class WebServices {

    static final String GET_BLACK_LIST_VISITORS = "blacklist/get_all_blacklist";
    static final String BASE_URL = "http://103.21.52.30:8085/e-visitor-system/api/";
    //static final String BASE_URL = "http://192.168.43.58:8085/api/";

    static final String LOGIN_AUTH = "authenticate";
    static final String GET_USER_DETAIL = "usermaster/get_user_details"; //?username=ram
    static final String ADD_GUEST = "guest/register_guest";
    static final String GET_HOUSE_LIST = "premise/get_all_flat_list"; //?accountId=1&search=a
    static final String GET_HOST_LIST = "resident/get_resident_by_flat_id"; //?accountId=1&flatId=6

    static final String GUEST_SEND_NOTIFICTION = "notification/send_notification";

    static final String GET_EXPECTED_GUEST_LIST = "guest/get_all_booked_guest";
    static final String GET_EXPECTED_SP_LIST = "serviceprovider/get_all_expected_service_provider";
    static final String GET_REGISTERED_HOUSE_KEEPING_LIST = "staff/get_all_staff"; //?accountId=1&page=0&size=10&type=expected&search=a

    static final String GET_GUEST_CHECKIN_LIST = "guest/get_guest_check_in_check_out";
    static final String GET_HOUSEKEEPING_CHECKIN_LIST = "staff/get_staff_check_in";
    static final String GET_SERVICE_PROVIDER_CHECKIN_LIST = "serviceprovider/get_service_provider_check_in_check_out";

    static final String HOUSE_KEEPING_CHECKIN_CHECKOUT = "staff/register_staff_check_in";
    static final String CHECKIN_CHECKOUT = "guest/check_in_check_out";

    private WebServices() {
    }
}
