package com.j9soft.poc.alarms;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static com.j9soft.poc.alarms.RaasDaoCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RaasDaoCassandraBootstrapTest {
    private static RaasDaoCassandraTestConfiguration testConfig;
    private RaasDao cassandraDao;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        // Connect to the embedded DB.
        testConfig = new RaasDaoCassandraTestConfiguration(true);
    }

    @AfterClass
    public static void cleanup() {
        testConfig.close();
//        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra(RaasDaoCassandra.KEYSPACE_NAME,
//                new String[0]);  // required because we have another Test class in this test run
    }

    @Before
    public void initDao() {
        // Create bean to be tested.
        cassandraDao = testConfig.getDao();
    }

    @Test
    public void seq1_whenTableMissing_thenCreateEmpty() {
        // Let's query. DB is empty, so cassandraDao should create the table, otherwise there will be an exception.
        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, isEmptyOrNullString());
    }

    @Test
    public void seq2_whenUpsertingAMissingAlarm_thenCreateIt() {

        cassandraDao.createOrPatchAlarm(EXISTING_ALARM.domain, EXISTING_ALARM.adapterName,
                EXISTING_ALARM.notificationIdentifier, EXISTING_ALARM.json);

        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }

}
