CREATE TABLE IF NOT EXISTS raas.raw_active_alarms(
  virtual_partition int, adapter_name text, notification_identifier text, 
  d text, 
  PRIMARY KEY( virtual_partition, adapter_name, notification_identifier)
  );

INSERT INTO raas.raw_active_alarms(virtual_partition, adapter_name, notification_identifier, d) VALUES(0, 'ma', 'kota', '{"moIdentifier":"kot"}');

