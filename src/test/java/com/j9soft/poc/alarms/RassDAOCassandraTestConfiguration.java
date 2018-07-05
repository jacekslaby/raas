package com.j9soft.poc.alarms;

//import org.springframework.context.annotation.Configuration;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;

//@Configuration
public class RassDAOCassandraTestConfiguration {

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

    public RassDAOCassandraTestConfiguration() {
        client = new CassandraConnector();
        client.connect("127.0.0.1", 9142);

        CQLDataLoader dataLoader = new CQLDataLoader(client.getSession());
        dataLoader.load(new ClassPathCQLDataSet("testDataSet.xml", RaasDAOCassandra.KEYSPACE_NAME));

        // "raas-cassandra-test-dataset.json", keyspace = "raas")

    }

    public RaasDAO getDAO() {
        return new RaasDAOCassandra(this.client.getSession());
    }

    public void close() {
        client.close();
    }
}
