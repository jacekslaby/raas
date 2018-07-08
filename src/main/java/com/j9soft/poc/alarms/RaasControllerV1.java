package com.j9soft.poc.alarms;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaasControllerV1 {

    @RequestMapping("/v1/rawalarms")
    public RawAlarm rawAlarms(@RequestParam(value = "notificationIdentifier") String notificationIdentifier) {
        return new RawAlarm(notificationIdentifier, "{\"ala\":\"ma kota\"}");
    }
}
