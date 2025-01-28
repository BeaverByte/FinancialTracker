package com.beaverbyte.financial_tracker_application.constants;

public class ApiEndpoints {
	public static final String BASE = "/api";

	public static final String AUTH = BASE + "/auth";

	public static final String SIGN_IN = "/signin";
	public static final String AUTH_SIGN_IN_URL = AUTH + SIGN_IN;

	public static final String SIGN_UP = "/signup";
	public static final String AUTH_SIGN_UP_URL = AUTH + SIGN_UP;

	public static final String SIGN_OUT = "/signout";
	public static final String AUTH_SIGN_OUT_URL = AUTH + SIGN_OUT;

	public static final String REFRESH_TOKEN = "/refreshtoken";
	public static final String AUTH_REFRESH_TOKEN_URL = AUTH + REFRESH_TOKEN;

	public static final String TEST = BASE + "/test";

	public static final String PUBLIC = "/all";
	public static final String TEST_PUBLIC_URL = TEST + PUBLIC;

	public static final String USER = "/user";
	public static final String TEST_USER_URL = TEST + USER;

	public static final String MOD = "/mod";
	public static final String TEST_MOD_URL = TEST + MOD;

	public static final String ADMIN = "/admin";
	public static final String TEST_ADMIN_URL = TEST + ADMIN;
}
