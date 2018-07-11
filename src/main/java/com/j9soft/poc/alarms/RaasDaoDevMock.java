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
    private final JSONValuePatchingComponent patcher = new JSONValuePatchingComponent();

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

            if (value != null) {
                // In this case we need to check whether an old value is non empty and patch it.
                //  (i.e. We do not simply overwrite, because we do not change alarm attributes not provided in new value.)
                //
                String oldValue = inMemoryMap.get(notificationIdentifier);
                value = patcher.patchOldValue(domain, adapterName, notificationIdentifier, value, oldValue);
            }

            inMemoryMap.put(notificationIdentifier, value);
        }
    }
}
