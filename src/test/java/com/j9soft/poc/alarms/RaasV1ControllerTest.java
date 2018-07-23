package com.j9soft.poc.alarms;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * I assume it is enough to test controller methods without testing HTTP wiring. (i.e. without TestRestTemplate, etc.)
 * (See also: https://spring.io/guides/gs/spring-boot/  @Autowired private TestRestTemplate template;  )
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RaasV1ControllerTest {

    private static final String DOMAIN = "controllerTest";
    private static final String ADAPTER_NAME = "controllerTestAdapter";
    private static final RawAlarmsPartitionDefinition PARTITION_DEFINITION = new RawAlarmsPartitionDefinition(DOMAIN, ADAPTER_NAME);

    private static final String ALARM_NOID = "eric2g:341";
    private static final String ALARM_JSON = "{\"severity\"=\"1\"}";
    private static final String ALARM_JSON_2 = "{\"severity\"=\"2\"}";

    private RaasV1 raas;
    private RaasDao raasDaoMock;

    @Before
    public void initRaas() {

        // Let's register what should be returned.
        //
        raasDaoMock = Mockito.mock(RaasDao.class);

        // Let's create the tested bean.
        raas = new RaasV1Controller(this.raasDaoMock);
    }

    @Test
    public void whenCreatingNewAlarm_itIsSavedToDao() {

        // Assert that a new alarm does not exist yet.
        assertThat(raas.rawAlarms(ALARM_NOID, PARTITION_DEFINITION), isEmptyOrNullString());

        // Let's upsert it.
        raas.patchRawAlarm(ALARM_NOID, PARTITION_DEFINITION, ALARM_JSON);

        // Let's verify that it was saved in Dao.
        verify(raasDaoMock).createOrPatchAlarm(DOMAIN, ADAPTER_NAME, ALARM_NOID, ALARM_JSON);
    }

    @Test
    public void whenGettingOneRawAlarm_itIsLoadedFromDao() {

        // Let's register what should be returned by DAO to our tested controller.
        //
        when(raasDaoMock.queryAlarm(DOMAIN, ADAPTER_NAME, ALARM_NOID)).thenReturn(ALARM_JSON);

        assertThat(raas.rawAlarms(ALARM_NOID, PARTITION_DEFINITION), is(ALARM_JSON));
    }

}