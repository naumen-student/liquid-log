package ru.naumen.sd40.log.parser;

import ru.naumen.DBConnector;
import ru.naumen.sd40.log.parser.dataset.DataSet;

public class DataSetUploader
{
    private DataSet dataSet = null;
    private DBConnector dbConnector;
    private long currentKey = -1;

    public DataSetUploader(DBConnector dbConnector)
    {
        this.dbConnector = dbConnector;
    }

    public DataSet get(long key) throws AlreadyProcessedKeyException
    {
        if (key == currentKey)
        {
            return dataSet;
        }
        if (key > currentKey)
        {
            if (currentKey != -1)
            {
                uploadDataSet();
            }
            currentKey = key;
            dataSet = new DataSet();
            return dataSet;
        }
        throw new AlreadyProcessedKeyException();
    }

    public void close()
    {
        uploadDataSet();
    }

    private void uploadDataSet()
    {
        dbConnector.store(currentKey, dataSet);
    }

    public class AlreadyProcessedKeyException extends Exception { }
}
