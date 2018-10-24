package ru.naumen.sd40.log.parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.InvalidParameterException;

import static org.mockito.Mockito.*;

public class DataSetControllerTests {

    private InfluxConnector connector;

    @Before
    public void setUp() {
        connector = Mockito.mock(InfluxConnector.class);

    }

    @Test
    public void testDataInDBAfterKeyIncreased() throws DBCloseException {
        try (DataSetController controller = new DataSetController(connector)) {
            controller.get(0L);
            controller.get(10L);
        }
        verify(connector).store(eq(0L), any(DataSet.class));
    }

    @Test(expected = InvalidParameterException.class)
    public void testRaiseExceptionIfWrongOrder() throws DBCloseException {
        try(DataSetController controller = new DataSetController(connector)) {
            controller.get(10L);
            controller.get(0L);
        }
    }

    @Test
    public void testDataInDBAfterClosing() throws DBCloseException {
        try (DataSetController controller = new DataSetController(connector)) {
            controller.get(0L);
            controller.get(10L);
        }

        verify(connector).store(eq(10L), any(DataSet.class));
    }

}
