package com.j9soft.poc.alarms;

//import org.springframework.context.annotation.Configuration;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;

//@Configuration
public class RaasDaoCassandraTestConfiguration {

    public static class AlarmDetails {
        String domain;
        String adapterName;
        String notificationIdentifier;
        String json;

        public AlarmDetails(String domain, String adapterName, String notificationIdentifier, String json) {
            this.domain = domain;
            this.adapterName = adapterName;
            this.notificationIdentifier = notificationIdentifier;
            this.json = json;
        }
    }

    public static final AlarmDetails EXISTING_ALARM =
            new AlarmDetails("ala", "ma", "kota", "kot ma ale");

    private CassandraConnector client;

    public RaasDaoCassandraTestConfiguration(boolean withEmptyDatabase) {
        client = new CassandraConnector();
        client.connect("127.0.0.1", 9142);

        if (!withEmptyDatabase) {
            // We should populate the database with sample data.
            CQLDataLoader dataLoader = new CQLDataLoader(client.getSession());
            dataLoader.load(new ClassPathCQLDataSet("testDataSet.xml", RaasDaoCassandra.KEYSPACE_NAME));
        }
    }

    public RaasDao getDao() {
        return new RaasDaoCassandra(this.client.getSession());
    }

    public void close() {
        client.close();
    }
}
