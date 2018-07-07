package com.j9soft.poc.alarms;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;

import java.io.IOException;

import static com.j9soft.poc.alarms.RassDAOCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class RaasDAOCassandraTest {

    private static RassDAOCassandraTestConfiguration testConfig;
    private RaasDAO cassandraDAO;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        // We do not use them and yet we must execute it.
        //  (otherwise there is an NPE in cleanDataEmbeddedCassandra() in cleanup() code later)
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = EmbeddedCassandraServerHelper.getSession();

        // Connect to the embedded DB.
        testConfig = new RassDAOCassandraTestConfiguration(false);
    }

    @Before
    public void initDAO() {
        // Create bean to be tested.
        cassandraDAO = testConfig.getDAO();
    }

    @Test
    public void whenGettingAnExistingAlarm_thenReturnIt() {
        String json = cassandraDAO.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }

    @Test
    public void whenUpsertingAMissingAlarm_thenCreateIt() {
        fail("@TODO");
    }

    @Test
    public void whenUpsertingAnExistingAlarm_thenUpdateIt() {
        fail("@TODO");
    }

    @Test
    public void whenTableExists_thenDataShouldSurviveDAOReconnect() {
        // Disconnect.
        testConfig.close();
        // Connect again to the embedded DB.  (do not create test data again !)
        testConfig = new RassDAOCassandraTestConfiguration(false);
        cassandraDAO = testConfig.getDAO();

        String json = cassandraDAO.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }


    @AfterClass
    public static void cleanup() {
        testConfig.close();

        // required because we have another Test class in this test run
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra(RaasDAOCassandra.KEYSPACE_NAME);
    }
}
