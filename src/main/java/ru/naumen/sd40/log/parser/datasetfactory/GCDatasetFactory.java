package ru.naumen.sd40.log.parser.datasetfactory;

public class GCDatasetFactory implements DataSetFactory<GCDataSet> {
    @Override
    public GCDataSet create() {
        return new GCDataSet();
    }
}
