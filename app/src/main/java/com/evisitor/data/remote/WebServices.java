package com.evisitor.data.remote;

import com.evisitor.BuildConfig;

/**
 * Created by Priyanka Joshi on 14-07-2020.
 */
class WebServices {
    public static final String GET_SP_BY_QRCODE = "serviceprovider/get_service_provider_by_qrcode";//get_service_provider_by_qrcode?accountId=1&qrCode=58397c18-4fd7-4a09-88ee-ddabaa7a5294.png
    static final String BASE_URL = BuildConfig.BASE_URL + "e-visitor-system/api/";
    static final String BASE_PLATE_RECOGNISER = "https://api.platerecognizer.com/v1/";
    //static final String BASE_URL = BuildConfig.BASE_URL + "api/"; //Local Server
    static final String PLATE_READER = "plate-reader/";
    static final String LOGIN_AUTH = "authenticate";
    static final String GET_USER_DETAIL = "usermaster/get_user_details"; //?username=ram
    static final String GET_GUEST_CONFIGURATION = "accountconfiguration/get_field_configuration_for_mobile"; //?accountId=1
    static final String GET_CONFIGURATIONS = "accountconfiguration/get_configuration_by_account_id"; //?accountId=1
    static final String ADD_GUEST = "guest/register_guest";
    static final String ADD_SP = "serviceprovider/register_service_provider";
    static final String GET_HOUSE_LIST = "premise/get_all_flat_list"; //?accountId=1&search=a
    static final String GET_HOST_LIST = "resident/get_resident_by_flat_id"; //?accountId=1&flatId=6
    static final String GUEST_SEND_NOTIFICATION = "notification/send_notification";
    static final String CHECK_GUEST_STATUS = "guest/check_blacklist_guest/"; //?accountId=1&documentId=44444
    static final String GET_EXPECTED_GUEST_LIST = "guest/get_all_booked_guest";
    static final String GET_EXPECTED_SP_LIST = "serviceprovider/get_all_expected_service_provider";
    static final String GET_REGISTERED_HOUSE_KEEPING_LIST = "staff/get_all_staff"; //?accountId=1&page=0&size=10&type=expected&search=a
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
    static final String GET_COMPANY_SUGGESTIONS = "usermaster/get_company_list";//http://localhost:8085/api/usermaster/get_company_list?search=\
    /*Commercial System Api's*/
    static final String ADD_COMMERCIAL_GUEST = "guest/register_commercial_guest";
    static final String GET_EXPECTED_COMMERCIAL_GUEST_LIST = "guest/get_all_commercial_booked_guest";//?page=0&size=10&sortBy=&direction=&search=&accountId=1
    static final String GET_COMMERCIAL_GUEST_CHECKIN_CHECKOUT_LIST = "guest/get_commercial_guest_check_in_check_out"; //?page=0&size=10&sortBy=&direction=&search=&accountId=1&type=CHECK_IN
    static final String GET_ALL_REGISTERED_COMMERCIAL_STAFF = "employee/get_all_staff";//http://localhost:8085/api/employee/get_all_staff?accountId=1&type=expected
    static final String GET_COMMERCIAL_CHECKIN_CHECKOUT_STAFF = "employee/get_staff_check_in";//http://localhost:8085/api/employee/get_staff_check_in?accountId=1&type=CHECK_OUT&search=a
    static final String COMMERCIAL_STAFF_CHECK_IN_OUT = "employee/check_in_check_out";//http://localhost:8085/api/employee/check_in_check_out
    static final String GET_COMMERCIAL_STAFF = "employee/get_staff_list"; //?accountId=1
    static final String COMMERCIAL_SEND_NOTIFICATION = "notification/send_commercial_notification";
    static final String GET_COMMERCIAL_NOTIFICATION = "notification/get_commercial_notification";//http://localhost:8085/api/notification/get_commercial_notification?accountId=1&username=sohan
    static final String GET_STAFF_BY_QR_CODE = "employee/get_staff_by_qr_code";//http://localhost:8085/api/employee/get_staff_by_qr_code?accountId=1&qrCode=fa3b384b-df4d-41a1-8f03-b4f3401f798c
    static final String GET_RESIDENT_BY_QR_CODE = "resident/get_resident_by_qr_code";//http://localhost:8085/api/resident/get_resident_by_qr_code?accountId=2&qrCode=fad58264-b5b9-4856-930b-3d163a4ee8a5
    static final String GET_VISITOR_BY_QR_CODE = "guest/get_visitor_by_qr_code";
    static final String POST_TEST_PASSWORD_REQUEST = "usermaster/generate_password_link";//http://localhost:8085/api/usermaster/generate_password_link
    /*Filter/Search Visitor*/
    static final String GET_FILTER_VISITOR_INFO = "guest/get_visitor_info";

    static final String ENABLE_DEVICE_NOTIFICATION = "notification/enable_device_notification";//http://localhost:8085/api/notification/enable_device_notification

    static final String DEVICE_NOTIFICATION_STATUS = "notification/device_notification_status";//http://localhost:8085/api/notification/device_notification_status

    static final String SEND_PANIC_NOTIFICATION = "notification/send_panic_notification";//http://localhost:8085/api/notification/send_panic_notification

    static final String RESIDENT_CHECK_IN_OUT = "resident/check_in_check_out";//http://localhost:8085/api/resident/check_in_check_out

    static final String GET_IMAGE_BY_NAME = "guest/get_base64_by_image_name";//http://197.220.114.46:8085/e-visitor-system/api/guest/get_base64_by_image_name?imageName=bdadff88-06c9-485a-8aef-5533326143e5.png

    static final String MRZ_DATA_EXTRACTION = "http://197.220.114.46:5000/api/v1/mrz";

    private WebServices() {
    }
}
