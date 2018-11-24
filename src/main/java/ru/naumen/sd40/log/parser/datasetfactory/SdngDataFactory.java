package ru.naumen.sd40.log.parser.datasetfactory;

public class SdngDataFactory implements DataSetFactory<SdngDataSet> {
    @Override
    public SdngDataSet create() {
        return new SdngDataSet();
    }
}
