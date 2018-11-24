package ru.naumen.sd40.log.parser.datasetfactory;

public class TopDataSetFactory implements DataSetFactory<TopDataSet> {
    @Override
    public TopDataSet create() {
        return new TopDataSet();
    }
}
