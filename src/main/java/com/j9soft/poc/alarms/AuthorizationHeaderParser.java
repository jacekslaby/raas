package com.j9soft.poc.alarms;

/*
 * https://jwt.io/introduction/
 *  typically in the Authorization header using the Bearer schema. The content of the header should look like the following:
 *    Authorization: Bearer <token>
 *
 * https://aboullaite.me/spring-boot-token-authentication-using-jwt/
 *
 */
public interface AuthorizationHeaderParser {

    AuthorizationHeaderClaims parse(String authorizationHeader);
}
