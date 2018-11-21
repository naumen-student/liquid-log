package ru.naumen.sd40.log.parser.datasetfactory;

import ru.naumen.data.GcStorage;

public class GCDataSet implements DataSet {
    private GcStorage storage;

    public GCDataSet() {
        storage = new GcStorage();
    }

    public GcStorage getStorage() {
        return storage;
    }
}
