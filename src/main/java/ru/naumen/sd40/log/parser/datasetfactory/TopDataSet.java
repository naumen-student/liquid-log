package ru.naumen.sd40.log.parser.datasetfactory;

import ru.naumen.data.TopStorage;

public class TopDataSet implements DataSet {
    private TopStorage storage;

    public TopDataSet() {
        storage = new TopStorage();
    }


    public TopStorage getStorage() {
        return storage;
    }
}
