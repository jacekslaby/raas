package com.j9soft.poc.alarms;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;

import java.io.IOException;

import static com.j9soft.poc.alarms.RassDaoCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class RaasDaoCassandraTest {

    private static RassDaoCassandraTestConfiguration testConfig;
    private RaasDao cassandraDao;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        // We do not use them and yet we must execute it.
        //  (otherwise there is an NPE in cleanDataEmbeddedCassandra() in cleanup() code later)
        Cluster cluster = EmbeddedCassandraServerHelper.getCluster();
        Session session = EmbeddedCassandraServerHelper.getSession();

        // Connect to the embedded DB.
        testConfig = new RassDaoCassandraTestConfiguration(false);
    }

    @Before
    public void initDao() {
        // Create bean to be tested.
        cassandraDao = testConfig.getDao();
    }

    @Test
    public void whenGettingAnExistingAlarm_thenReturnIt() {
        String json = cassandraDao.queryAlarm(
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
    public void whenTableExists_thenDataShouldSurviveDaoReconnect() {
        // Disconnect.
        testConfig.close();
        // Connect again to the embedded DB.  (do not create test data again !)
        testConfig = new RassDaoCassandraTestConfiguration(false);
        cassandraDao = testConfig.getDao();

        String json = cassandraDao.queryAlarm(
                EXISTING_ALARM.domain, EXISTING_ALARM.adapterName, EXISTING_ALARM.notificationIdentifier);

        assertThat(json, is(EXISTING_ALARM.json));
    }


    @AfterClass
    public static void cleanup() {
        testConfig.close();

        // required because we have another Test class in this test run
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra(RaasDaoCassandra.KEYSPACE_NAME);
    }
}
