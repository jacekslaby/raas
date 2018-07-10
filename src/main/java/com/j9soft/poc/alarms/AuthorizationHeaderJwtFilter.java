package com.j9soft.poc.alarms;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * https://aboullaite.me/spring-boot-token-authentication-using-jwt/
 */

// This Filter is used in dev mode, i.e. in development environments. (as "default")
//  (BTW: In prod (i.e. production) mode a different one may be used. E.g. in case when no other filtering is installed to verify token's signature.)
//
@Profile("default")
@Component
public class AuthorizationHeaderJwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            filterChain.doFilter(servletRequest, servletResponse);
        } else {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                onMissingAuthorizationHeader(request);

            } else {

                final String token = authHeader.substring(7);
                final Claims claims;

                try {
                    // @TODO do something about secretkey
                    claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
                } catch (final SignatureException e) {
                    throw new ServletException("Invalid token");
                }

                final String domain = claims.get("domain", String.class);
                final String adapterName = claims.get("adapterName", String.class);
                request.setAttribute("claims", new AuthorizationHeaderClaims(domain, adapterName));
            }

            filterChain.doFilter(request, response);
        }
    }

    void onMissingAuthorizationHeader(HttpServletRequest request) throws ServletException {
        // In development mode, if client does not provide JWT token, then we assume defaults.
        //  (In order to make it easier for newbie developers.)
        //
        request.setAttribute("claims", new AuthorizationHeaderClaims("dev", "AdapterTest"));
    }
}
