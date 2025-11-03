package com.k1ts.security;

public class SecurityConstants {
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60; //60 minutes
    public static final long REFRESH_TOKEN_EXPIRATION_TIME =  86400L * 1000L * 30L; //30 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_COOKIE_PATH = "/"; // /api/v1/auth
}