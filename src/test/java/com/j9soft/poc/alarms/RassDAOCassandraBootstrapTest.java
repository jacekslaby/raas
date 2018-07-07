package com.j9soft.poc.alarms;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.j9soft.poc.alarms.RassDAOCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.fail;

public class RassDAOCassandraBootstrapTest {
    private static RassDAOCassandraTestConfiguration testConfig;
    private RaasDAO cassandraDAO;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        // Connect to the embedded DB.
        testConfig = new RassDAOCassandraTestConfiguration(true);
    }

    @Before
    public void initDAO() {
        // Create bean to be tested.
        cassandraDAO = testConfig.getDAO();
    }

    @Test
    public void whenTableMissing_thenCreateEmpty() {
        // Let's query. DB is empty, so cassandraDAO should create the table, otherwise there will be an exception.
        String json = cassandraDAO.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, isEmptyOrNullString());
    }

    @AfterClass
    public static void cleanup() {
        testConfig.close();
//        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra(RaasDAOCassandra.KEYSPACE_NAME,
//                new String[0]);  // required because we have another Test class in this test run
    }
}
