package com.j9soft.poc.alarms;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

// This Parser is used in dev mode, i.e. in development environments. (as "default")
//  (BTW: In prod (i.e. production) mode a different one may be used. E.g. in case when no other filtering is installed to verify token's signature.)
//
@Profile("default")
@Service
public class AuthorizationHeaderParserNotVerifying implements AuthorizationHeaderParser {

    @Override
    public AuthorizationHeaderClaims parse(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new AuthorizationHeaderClaims(null, null);
        }

        final String token = authorizationHeader.substring(7);

        // @TODO Claims claims = Jwts.parser().parseClaimsJws(token).getBody();  // # Without setSigningKey("secret") because we do not verify here.
        Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();

        final String domain = claims.get("domain", String.class);
        final String adapterName = claims.get("adapterName", String.class);

        return new AuthorizationHeaderClaims(domain, adapterName);
    }
}
