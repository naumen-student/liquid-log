package ru.naumen.sd40.log.parser;

import java.security.InvalidParameterException;

public class DataSetController {

    private InfluxConnector connector;
    private long currentKey = -1;
    private DataSet currentDataSet;

    public DataSetController(InfluxConnector connector) {

        this.connector = connector;
    }

    public DataSet get(long key) throws InvalidParameterException {
        if (key == currentKey) {
            return currentDataSet;
        }

        if (key > currentKey) {
            if (currentKey != -1) {
                connector.store(currentKey, currentDataSet);
            }
            currentKey = key;
            currentDataSet = new DataSet();
            return currentDataSet;
        }
        throw new InvalidParameterException(String.format("Given key: %d is already stored", key));
    }

    public void close() {
        connector.store(currentKey, currentDataSet);
        connector.close();
    }
}
