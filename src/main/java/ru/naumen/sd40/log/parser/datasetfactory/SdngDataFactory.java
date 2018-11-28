package ru.naumen.sd40.log.parser.datasetfactory;

import org.springframework.stereotype.Component;

@Component
public class SdngDataFactory implements DataSetFactory<SdngDataSet> {
    @Override
    public SdngDataSet create() {
        return new SdngDataSet();
    }
}
