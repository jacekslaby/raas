package com.j9soft.poc.alarms;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;

import java.io.IOException;

import static com.j9soft.poc.alarms.RassDAOCassandraTestConfiguration.EXISTING_ALARM;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class RaasDAOCassandraTest {

    private static RassDAOCassandraTestConfiguration testConfig;
    private RaasDAO cassandraDAO;

    @BeforeClass
    public static void init() throws IOException, TTransportException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
        // Connect to DB.
        testConfig = new RassDAOCassandraTestConfiguration();
    }

    @Before
    public void initDAO() {
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

    }

    @Test
    public void whenUpsertingAnExistingAlarm_thenUpdateIt() {

    }

    @AfterClass
    public static void cleanup() {
        testConfig.close();
        //EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
