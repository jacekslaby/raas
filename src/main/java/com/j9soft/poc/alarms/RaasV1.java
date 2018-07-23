package com.j9soft.poc.alarms;

/**
 * Interface defining functionality provided by API (version 1) of Raw Active Alarms Store.
 */
public interface RaasV1 {

    String rawAlarms(String notificationIdentifier, RawAlarmsPartitionDefinition partitionDefinition);

    void patchRawAlarm(String notificationIdentifier, RawAlarmsPartitionDefinition partitionDefinition,
                         String valueObjectAsJson);
}
