package com.j9soft.poc.alarms;

public interface RaasDao {
    public String queryAlarm(String domain, String adapterName, String notificationIdentifier);
}
