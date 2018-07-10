package com.j9soft.poc.alarms;

public interface RaasDao {
    String queryAlarm(String domain, String adapterName, String notificationIdentifier);

    void createOrPatchAlarm(String domain, String adapterName, String notificationIdentifier, String value);
}
