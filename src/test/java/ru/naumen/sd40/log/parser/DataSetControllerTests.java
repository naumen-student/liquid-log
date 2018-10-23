package ru.naumen.sd40.log.parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.InvalidParameterException;

import static org.mockito.Mockito.*;

public class DataSetControllerTests {

    private InfluxConnector connector;
    private DataSetController controller;

    @Before
    public void setUp() {
        connector = Mockito.mock(InfluxConnector.class);
        controller = new DataSetController(connector);

    }

    @Test
    public void testDataInDBAfterKeyIncreased() {
        controller.get(0L);
        controller.get(10L);

        verify(connector).store(eq(0L), any(DataSet.class));
    }

    @Test(expected = InvalidParameterException.class)
    public void testRaiseExceptionIfWrongOrder() {
        controller.get(10L);
        controller.get(0L);


    }

    @Test
    public void testDataInDBAfterClosing() {
        controller.get(0L);
        controller.get(10L);
        controller.close();

        verify(connector).store(eq(10L), any(DataSet.class));
    }

}
