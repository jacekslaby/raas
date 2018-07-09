package com.j9soft.poc.alarms;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

// This Dao is used in dev mode, i.e. in development environments.
//  (BTW: In prod (i.e. production) mode a different Dao is used. One which connects to a real DB.)
//
@Profile("default")
@Service
public class RaasDaoDevMock implements RaasDao {

    @Override
    public String queryAlarm(String domain, String adapterName, String notificationIdentifier) {
        return "{\"notificationIdentifier\"=" + notificationIdentifier + "\"}";
    }
}
