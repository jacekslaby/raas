package com.j9soft.poc.alarms;

public interface RaasV1 {

    String rawAlarms(String notificationIdentifier, RawAlarmsPartitionDefinition partitionDefinition);

    void patchRawAlarm(String notificationIdentifier, RawAlarmsPartitionDefinition partitionDefinition,
                         String valueObjectAsJson);
}
