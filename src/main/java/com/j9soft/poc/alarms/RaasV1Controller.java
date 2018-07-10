package com.j9soft.poc.alarms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

// See https://spring.io/guides/gs/spring-boot/

@RestController
public class RaasV1Controller implements RaasV1 {

    final private Logger logger = LoggerFactory.getLogger(RaasV1Controller.class);

    // @Autowired  - is not used because:
    // https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
    // "Don’t use field injection as it just makes your tests harder to write."
    //
    private RaasDao raasDao;

    @Autowired
    private AuthorizationHeaderParser authorizationHeaderParser;


    @Autowired
    RaasV1Controller(RaasDao raasDao) {

        this.raasDao = raasDao;
    }

    @Override
    @GetMapping
    @RequestMapping("/v1/rawalarms")
    public String rawAlarms(@RequestParam(value = "notificationIdentifier") String notificationIdentifier) {

        return this.raasDao.queryAlarm("todo", "todo", notificationIdentifier);

        //return "{\"ala\":\"ma kota\"}";
    }

    // @Override  // @TODO I should define a correct name for the interface
    @GetMapping   // @TODO It should be: @PutMapping
    @RequestMapping("/v1/rawalarms/{notificationIdentifier}")
    public String putAlarm(@PathVariable("notificationIdentifier") String notificationIdentifier,
            @RequestHeader(value = "authorization",
                    defaultValue = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkb21haW4iOiJkZXYiLCJhZGFwdGVyTmFtZSI6IkFkYXB0ZXJUZXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.TgxX8dxUgehgcJF6aYIcqwFE308OCHFm4bPCCefJwzM")
                      // Default value generated on https://jwt.io/ with "secret".
                    String authorizationHeader ) {

        AuthorizationHeaderClaims claims = this.authorizationHeaderParser.parse(authorizationHeader);

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(claims);

        } catch (JsonProcessingException e) {
            logger.info("putAlarm", authorizationHeader, e);
            return "Authorization Header exception was logged.";
        }
    }

    @PostConstruct
    public void init() {
        Assert.notNull(raasDao, "raasDao is null!");
        logger.info("raasDao is not null - OK");
    }

}