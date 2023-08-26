package com.petstore.utils;

/**
 * This interface contains endpoint constants for the Pet Store API.
 * It defines various API endpoints for different functionalities.
 */
public interface Endpoints {
    // Store Endpoints
    public static final String STORE = "/store";
    public static final String STORE_INVENTORY = STORE + "/inventory";
    public static final String ORDER = STORE + "/order";
    public static final String ORDER_BY_ID = ORDER + "/{orderId}";

    // User Endpoints
    public static final String USER = "/user";
    public static final String USER_BY_USERNAME = USER + "/{username}";
    public static final String USER_CREATE_WITH_LIST = USER + "/createWithList";
    public static final String USER_LOGIN = USER + "/login";
}

