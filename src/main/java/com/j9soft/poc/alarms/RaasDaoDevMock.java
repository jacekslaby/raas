package com.j9soft.poc.alarms;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// This Dao is used in dev mode, i.e. in development environments.
//  (BTW: In prod (i.e. production) mode a different Dao is used. One which connects to a real DB.)
//
@Profile("default")
@Service
public class RaasDaoDevMock implements RaasDao {

    private final Map<String, String> inMemoryMap = new HashMap<>();

    @Override
    public String queryAlarm(String domain, String adapterName, String notificationIdentifier) {
        String resultJsonValue;
        synchronized (inMemoryMap) {
            resultJsonValue = inMemoryMap.get(notificationIdentifier);
        }

        return resultJsonValue;
    }

    @Override
    public void createOrPatchAlarm(String domain, String adapterName, String notificationIdentifier, String value) {
        synchronized (inMemoryMap) {
            String resultJsonValue = inMemoryMap.get(notificationIdentifier);

            if (resultJsonValue != null) {
                // @TODO we should patch it, but it is complicated, so left for now
            }

            inMemoryMap.put(notificationIdentifier, value);
        }
    }
}
