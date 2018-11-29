package ru.naumen.sd40.log.parser.datasetfactory;

import org.springframework.stereotype.Component;

@Component
public class ErrorDataSetFactory implements DataSetFactory<ErrorDataSet> {
    @Override
    public ErrorDataSet create() {
        return new ErrorDataSet();
    }
}
