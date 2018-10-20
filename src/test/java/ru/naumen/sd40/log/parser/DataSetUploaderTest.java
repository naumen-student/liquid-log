package ru.naumen.sd40.log.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.naumen.DBConnector;
import ru.naumen.sd40.log.parser.dataset.DataSet;

import static org.mockito.Mockito.*;

public class DataSetUploaderTest
{
    private DBConnector dbConnector;
    private DataSetUploader uploader;

    @Before
    public void setUp()
    {
        dbConnector = Mockito.mock(DBConnector.class);
        uploader = new DataSetUploader(dbConnector);
    }

    @Test
    public void mustUploadWhenKeyIncreased() throws DataSetUploader.AlreadyProcessedKeyException
    {
        //when
        uploader.get(0);
        uploader.get(1);

        //then
        verify(dbConnector).store(eq(0L), any(DataSet.class));
    }

    @Test
    public void mustUploadWhenClosed() throws DataSetUploader.AlreadyProcessedKeyException
    {
        //when
        uploader.get(0);
        uploader.close();

        //then
        verify(dbConnector).store(eq(0L), any(DataSet.class));
    }

    @Test(expected = DataSetUploader.AlreadyProcessedKeyException.class)
    public void mustThrowIfIncorrectOrder() throws DataSetUploader.AlreadyProcessedKeyException {
        //when
        uploader.get(1);
        uploader.get(0);
    }
}