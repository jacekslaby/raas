package com.j9soft.poc.alarms;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Map;

import static com.j9soft.poc.alarms.RaasDaoCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RaasDaoCassandraTest {

    private static RaasDaoCassandraTestConfiguration testConfig;
    private RaasDao cassandraDao;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        // Connect to the embedded DB.
        testConfig = new RaasDaoCassandraTestConfiguration(false);


        // Unwanted code, but... :/
        //
        // We do not use these variables and yet we must execute those methods.
        //  (otherwise there is an NPE in cleanDataEmbeddedCassandra() in cleanup() code later)
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = EmbeddedCassandraServerHelper.getSession();
    }

    @AfterClass
    public static void cleanup() {
        testConfig.close();

        // required because we have another Test class in this test run
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra(RaasDaoCassandra.KEYSPACE_NAME);
    }

    @Before
    public void initDao() {
        // Create bean to be tested.
        this.cassandraDao = testConfig.getDao();
    }

    @Test
    public void whenGettingAnExistingAlarm_thenReturnIt() {
        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }

    @Test
    public void whenTableExists_thenDataShouldSurviveDaoReconnect() {
        // Disconnect.
        testConfig.close();
        // Connect again to the embedded DB.  (do not create test data again !)
        testConfig = new RaasDaoCassandraTestConfiguration(false);
        cassandraDao = testConfig.getDao();

        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }

    @Test
    public void whenUpsertingAnExistingAlarm_thenUpdateIt() throws IOException {
        final String newJson = "{\"additionalText\":\"serious stuff\"}";

        cassandraDao.createOrPatchAlarm(EXISTING_ALARM.domain, EXISTING_ALARM.adapterName,
                EXISTING_ALARM.notificationIdentifier, newJson);

        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> newValue = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        assertThat(newValue.get("additionalText"), is("serious stuff"));
    }

}
