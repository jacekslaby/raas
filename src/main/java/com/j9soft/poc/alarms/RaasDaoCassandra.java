package com.j9soft.poc.alarms;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class RaasDaoCassandra implements RaasDao {

    static final String KEYSPACE_NAME = "raas";
    private static final String REPLICATION_STRATEGY = "SimpleStrategy";  // @TODO configurable
    private static final int NUMBER_OF_REPLICAS = 1;  // @TODO configurable

    private static final String TABLE_NAME_RAW_ACTIVE_ALARMS = "raw_active_alarms";

    private Session session;

    public RaasDaoCassandra(Session session) {
        this.session = session;
        this.createKeyspaceIfMissing();
        this.createTableIfMissing();
    }

    private void createKeyspaceIfMissing() {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append(KEYSPACE_NAME)
                .append(" WITH replication = {").append("'class':'").append(REPLICATION_STRATEGY)
                .append("','replication_factor':").append(NUMBER_OF_REPLICAS).append("};");


        final String query = sb.toString();

        ResultSet rs = session.execute(query);
    }

    private void createTableIfMissing() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(KEYSPACE_NAME).append(".")
                .append(TABLE_NAME_RAW_ACTIVE_ALARMS).append("(")
                .append("virtual_partition int,")
                .append("adapter_name text,")
                .append("notification_identifier text,")
                .append("d text,")
                .append("PRIMARY KEY( virtual_partition, adapter_name, notification_identifier));");

        final String query = sb.toString();

        ResultSet rs = session.execute(query);
    }

    @Override
    public String queryAlarm(String domain, String adapterName, String notificationIdentifier) {
        StringBuilder sb = new StringBuilder("SELECT d FROM ")
                .append(KEYSPACE_NAME).append(".raw_active_alarms")
                .append(" WHERE virtual_partition = ").append("0")
                .append(" AND adapter_name = '").append(adapterName).append("'")
                .append(" AND notification_identifier = '").append(notificationIdentifier).append("'")
                .append(";");

        final String query = sb.toString();

        ResultSet rs = session.execute(query);
        Row row = rs.one();

        if (row == null) {
            return null;
        }

        return row.getString(0);
    }

    @Override
    public void createOrPatchAlarm(String domain, String adapterName, String notificationIdentifier, String value) {

        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(KEYSPACE_NAME).append(".raw_active_alarms")
                .append(" (virtual_partition, adapter_name, notification_identifier, d) VALUES (?,?,?,?);");

        final String query = sb.toString();

        PreparedStatement prepared = session.prepare(query);

        session.execute( prepared.bind(0, adapterName, notificationIdentifier, value) );
    }
}
