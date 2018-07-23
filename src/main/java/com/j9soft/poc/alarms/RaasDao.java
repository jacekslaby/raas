package com.j9soft.poc.alarms;

/**
 * Interface defining what functionality is provided by a data access layer.
 */
public interface RaasDao {
    String queryAlarm(String domain, String adapterName, String notificationIdentifier);

    void createOrPatchAlarm(String domain, String adapterName, String notificationIdentifier, String value);
}
