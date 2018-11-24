package ru.naumen.sd40.log.parser.datasetfactory;

public class ActionDataFactory implements DataSetFactory<ActionDataSet> {
    @Override
    public ActionDataSet create() {
        return new ActionDataSet();
    }
}
