package ru.naumen.sd40.log.parser.datasetfactory;

import org.springframework.stereotype.Component;

@Component
public class ActionDataFactory implements DataSetFactory<ActionDataSet> {
    @Override
    public ActionDataSet create() {
        return new ActionDataSet();
    }
}
