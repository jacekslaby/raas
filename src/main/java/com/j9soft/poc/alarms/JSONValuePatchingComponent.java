package com.j9soft.poc.alarms;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONValuePatchingComponent {

    private final ObjectMapper mapper = new ObjectMapper();

    public String patchOldValue(String domain, String adapterName, String notificationIdentifier, String newValue, String oldValue) {

        if (oldValue != null) {
            // It means that the previous value exists. We should patch it with new contents.  (i.e. new alarm attributes)

            Map<String, Object> oldValueAsMap;
            Map<String, Object> newValueAsMap;

            try {
                oldValueAsMap = mapper.readValue(oldValue, HashMap.class);
            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("Unsupported non-JSON value for alarm with Notification Identifier = '%s'. Old value = '%s'",
                                notificationIdentifier, oldValue), e);
            }
            try {
                newValueAsMap = mapper.readValue(newValue, HashMap.class);
            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("Unsupported non-JSON value for alarm with Notification Identifier = '%s'. New value = '%s'",
                                notificationIdentifier, newValue), e);
            }

            try {
                oldValueAsMap.putAll(newValueAsMap);  // we overwrite old attributes with new ones
                newValue = mapper.writeValueAsString(oldValueAsMap);   // we create an updated newValue

            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("Problem producing JSON value for alarm with Notification Identifier = %s",
                                notificationIdentifier), e);
            }
        }
        return newValue;
    }
}
