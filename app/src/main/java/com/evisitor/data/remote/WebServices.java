package com.evisitor.data.remote;

import com.evisitor.BuildConfig;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
class WebServices {

    static final String BASE_URL = BuildConfig.BASE_URL + "e-visitor-system/api/";
    //static final String BASE_URL = BuildConfig.BASE_URL + "api/"; //Local Server

    static final String LOGIN_AUTH = "authenticate";
    static final String GET_USER_DETAIL = "usermaster/get_user_details"; //?username=ram
    static final String GET_GUEST_CONFIGURATION = "accountconfiguration/get_field_configuration_for_mobile"; //?accountId=1
    static final String ADD_GUEST = "guest/register_guest";
    static final String ADD_COMMERCIAL_GUEST = "guest/register_commercial_guest";
    static final String ADD_SP = "serviceprovider/register_service_provider";
    static final String GET_HOUSE_LIST = "premise/get_all_flat_list"; //?accountId=1&search=a
    static final String GET_HOST_LIST = "resident/get_resident_by_flat_id"; //?accountId=1&flatId=6

    static final String GUEST_SEND_NOTIFICTION = "notification/send_notification";
    static final String CHECK_GUEST_STATUS = "guest/check_blacklist_guest"; //?accountId=1&documentId=44444

    static final String GET_EXPECTED_GUEST_LIST = "guest/get_all_booked_guest";
    static final String GET_EXPECTED_COMMERCIAL_GUEST_LIST = "guest/get_all_commercial_booked_guest";//?page=0&size=10&sortBy=&direction=&search=&accountId=1
    static final String GET_EXPECTED_SP_LIST = "serviceprovider/get_all_expected_service_provider";
    static final String GET_REGISTERED_HOUSE_KEEPING_LIST = "staff/get_all_staff"; //?accountId=1&page=0&size=10&type=expected&search=a

    static final String GET_COMMERCIAL_GUEST_CHECKIN_CHECKOUT_LIST = "guest/get_commercial_guest_check_in_check_out"; //?page=0&size=10&sortBy=&direction=&search=&accountId=1&type=CHECK_IN
    static final String GET_GUEST_CHECKIN_CHECKOUT_LIST = "guest/get_guest_check_in_check_out";
    static final String GET_HOUSEKEEPING_CHECKIN_CHECKOUT_LIST = "staff/get_staff_check_in";
    static final String GET_SERVICE_PROVIDER_CHECKIN_CHECKOUT_LIST = "serviceprovider/get_service_provider_check_in_check_out";

    static final String CHECKIN_CHECKOUT = "guest/check_in_check_out";
    static final String GET_VISITOR_COUNT = "guest/get_visitor_count"; //?accountId=1

    static final String GET_BLACK_LIST_VISITORS = "blacklist/get_all_blacklist";
    static final String GET_ALL_TRESPASSER_GUEST = "guest/get_all_trespasser_guest";
    static final String GET_ALL_TRESPSSER_SP = "serviceprovider/get_all_trespasser_service_provider";
    static final String GET_ALL_FLAG_VISITORS = "flag/get_all_flag_list"; //?page=0&size=10&sortBy=&direction=&search=&accountId=1&type=flag

    static final String GET_NOTIFICATIONS = "notification/get_device_notification";
    static final String READ_ALL_NOTIFICATIONS = "notification/read_notification"; //?username=test&accountId=1
    static final String GET_HOUSE_INFO = "premise/get_flat_level"; //?flatId=11&accountId=2
    static final String GET_PROPERTY_INFO = "account/get_account_by_id"; //?accountId=1

    static final String GET_REJECT_VISITORS = "guest/get_all_rejected_visitor"; //?accountId=1&residentId=1&type=guest

    static final String GET_PROFILES_SUGGESTIONS = "usermaster/get_profile_list";//http://localhost:8085/api/usermaster/get_profile_list?search=

    static final String GET_COMPANY_SUGGESTIONS = "usermaster/get_company_list";//http://localhost:8085/api/usermaster/get_company_list?search=

    private WebServices() {
    }
}
