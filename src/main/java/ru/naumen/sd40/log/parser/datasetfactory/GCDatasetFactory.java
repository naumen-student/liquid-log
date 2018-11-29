package ru.naumen.sd40.log.parser.datasetfactory;

import org.springframework.stereotype.Component;

@Component
public class GCDatasetFactory implements DataSetFactory<GCDataSet> {
    @Override
    public GCDataSet create() {
        return new GCDataSet();
    }
}
