package com.example.vuespringexample.jwt;

public class JWTConstant {
    public static final long EXPIRATION_TIME = 3600 * 24 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING_AUTHORIZATION = "Authorization";
    public static final String SECRET = "絶対に外部に漏れてはならない秘密の文字列";
}
