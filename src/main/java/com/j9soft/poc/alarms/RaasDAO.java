package com.j9soft.poc.alarms;

public interface RaasDAO {
    public String queryAlarm(String domain, String adapterName, String notificationIdentifier);
}
