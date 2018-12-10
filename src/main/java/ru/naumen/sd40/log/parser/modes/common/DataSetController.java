package ru.naumen.sd40.log.parser.modes.common;

import java.security.InvalidParameterException;

public class DataSetController<I extends DataSet> implements AutoCloseable {

    private I currentDataSet;
    private Holder<I> connector;
    private DataSetFactory<I> factory;

    private long currentKey = -1;

    public DataSetController(Holder<I> connector, DataSetFactory<I> factory) {

        this.connector = connector;
        this.factory = factory;
    }

    public I get(long key) throws InvalidParameterException {
        if (key == currentKey) {
            return currentDataSet;
        }

        if (key > currentKey) {
            if (currentKey != -1) {
                connector.store(currentKey, currentDataSet);
            }
            currentKey = key;
            currentDataSet = factory.create();
            return currentDataSet;
        }
        throw new InvalidParameterException(String.format("Given key: %d is already stored", key));
    }

    public void close() throws DBCloseException {
        connector.store(currentKey, currentDataSet);
        connector.close();
    }
}
