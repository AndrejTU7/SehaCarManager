package com.studioadriatic.sehacarmanager.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Andrej on 13.2.2016..
 */
public class Api {
    public static final String BASE_URL = "http://api.studioadriatic.com/seha/";
    private static final String GROUP_USER = "user/";
    private static final String GROUP_GCM = "gcm/";
    private static final String GROUP_LIST = "list/";
    private static final String GROUP_NEW = "new/";
    private static final String GROUP_UPDATE = "update/";
    private static final String GROUP_DELETE = "delete/";
    private static final String GROUP_MESSAGES = "messages/";
    /**
     * User to auth user on server while using API
     */
    private static final String SERVER_TOKEN = "server_token";
    /**
     * Parameter for sending email address
     */
    public static final String PARAM_EMAIL = "email";
    /**
     * Parameter for sending user password
     */
    public static final String PARAM_PASSWORD = "password";
    /**
     * Parameter for sending GCM token
     */
    public static final String PARAM_GCM_TOKEN = "gcm_token";
    /**
     * Parameter for sending user name
     */
    public static final String PARAM_NAME = "name";
    /**
     * Parameter for sending user type
     */
    public static final String PARAM_TYPE = "type";
    /**
     * Parameter for sending user image
     */
    public static final String PARAM_IMAGE = "image";
    /**
     * Parameter for sending car id
     */
    public static final String PARAM_CAR_ID = "car_id";
    /**
     * Parameter for sending car name
     */
    public static final String PARAM_CAR_NAME = "car_name";
    /**
     * Parameter for sending car registration
     */
    public static final String PARAM_CAR_REGISTRATION = "car_registration";
    /**
     * Api URL for user register
     */
    public static final String USER_REGISTRATION = GROUP_USER + "register";
    /**
     * Api URL for user login with username and password
     */
    public static final String USER_LOGIN = GROUP_USER + "login";
    /**
     * Api URL for user password reset
     */
    public static final String USER_PASSWORD_RESET = GROUP_USER + "password_reset";
    /**
     * Api URL for storing update token
     */
    public static final String GCM_UPDATE_TOKEN = GROUP_GCM + "update_token";
    /**
     * Api URL for user login with token
     */
    public static final String USER_LOGIN_TOKEN = GROUP_USER + "login_token";
    /**
     * Api URL for list of cars
     */
    public static final String LIST_CARS = GROUP_LIST + "cars";
    /**
     * Api URL for add new car
     */
    public static final String NEW_CAR = GROUP_NEW + "car";
    /**
     * Api URL for edit existing car
     */
    public static final String UPDATE_CAR = GROUP_UPDATE + "car";
    /**
     * Api URL for delete existing car
     */
    public static final String DELETE_CAR = GROUP_DELETE + "car";

    public static final String PARAM_LOCATION_NAME = "location_name";

    public static final String LIST_LOCATIONS = GROUP_LIST + "locations";

    public static final String UPDATE_LOCATION = GROUP_UPDATE + "location";
    public static final String PARAM_LOCATION_ADDRESS = "location_address";
    public static final String NEW_LOCATION = GROUP_NEW + "location";
    public static final String PARAM_LOCATION_ID = "location_id";
    public static final String DELETE_LOCATION = GROUP_DELETE + "location";
    public static final String LIST_ADMINS = GROUP_LIST + "admins";
    public static final String LIST_DRIVERS = GROUP_LIST + "drivers";
    public static final String LIST_CHATS = GROUP_LIST + "chats";
    public static final String MESSAGES_ID = GROUP_MESSAGES + "id";
    public static final String LIST_CONTACTS = GROUP_LIST + "contacts";
    public static final String LIST_MESSAGES = GROUP_LIST + "messages";
    public static final String PARAM_CHAT_ID = "chat_id";
    public static final String MESSAGES_SEND = GROUP_MESSAGES + "send";
    public static final String PARAM_MESSAGE = "message";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static RequestParams getRequestParams(String serverToken) {
        RequestParams params = new RequestParams();
        params.put(SERVER_TOKEN, serverToken);
        return params;
    }
}
