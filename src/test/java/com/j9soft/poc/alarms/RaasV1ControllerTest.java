package com.j9soft.poc.alarms;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class RaasV1ControllerTest {

    private static final String ALARM_NOID = "eric2g:341";
    private static final String ALARM_JSON = "{\"severity\"=\"1\"}";

    private RaasV1 raas;
    private RaasDao raasDaoMock;

    @Before
    public void initRaas() {

        // Let's register what should be returned.
        //
        this.raasDaoMock = Mockito.mock(RaasDao.class);

        // Let's create the tested bean.
        this.raas = new RaasV1Controller(this.raasDaoMock);
    }

    @Test
    public void whenGettingOneRawAlarm_resultIsJson() {

        // Let's register what should be returned.
        //
        when(raasDaoMock.queryAlarm("todo", "todo", ALARM_NOID)).thenReturn(ALARM_JSON);

        assertThat(this.raas.rawAlarms(ALARM_NOID), is(ALARM_JSON));
    }
}