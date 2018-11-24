package ru.naumen.sd40.log.parser.datasetfactory;

public class ErrorDataSetFactory implements DataSetFactory<ErrorDataSet> {
    @Override
    public ErrorDataSet create() {
        return new ErrorDataSet();
    }
}
