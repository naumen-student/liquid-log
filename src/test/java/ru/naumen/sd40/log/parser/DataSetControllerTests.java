package ru.naumen.sd40.log.parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.naumen.sd40.log.parser.modes.common.DBCloseException;
import ru.naumen.sd40.log.parser.modes.common.DataSetController;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataFactory;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.holder.SdngConnector;

import java.security.InvalidParameterException;

import static org.mockito.Mockito.*;

public class DataSetControllerTests {

    private SdngConnector connector;
    private SdngDataFactory factory;

    @Before
    public void setUp() {
        connector = Mockito.mock(SdngConnector.class);
        factory = new SdngDataFactory();

    }

    @Test
    public void testDataInDBAfterKeyIncreased() throws DBCloseException {
        try (DataSetController<SdngDataSet> dataSetController = new DataSetController<>(connector, factory)) {
            dataSetController.get(0L);
            dataSetController.get(10L);
        }
        verify(connector).store(eq(0L), any(SdngDataSet.class));
    }

    @Test(expected = InvalidParameterException.class)
    public void testRaiseExceptionIfWrongOrder() throws DBCloseException {
        try(DataSetController<SdngDataSet> dataSetController = new DataSetController<>(connector, factory)) {
            dataSetController.get(10L);
            dataSetController.get(0L);
        }
    }

    @Test
    public void testDataInDBAfterClosing() throws DBCloseException {
        try (DataSetController<SdngDataSet> dataSetController = new DataSetController<>(connector, factory)) {
            dataSetController.get(0L);
            dataSetController.get(10L);
        }

        verify(connector).store(eq(10L), any(SdngDataSet.class));
    }

}
