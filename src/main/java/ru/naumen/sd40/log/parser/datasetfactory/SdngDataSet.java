package ru.naumen.sd40.log.parser.datasetfactory;

import ru.naumen.data.ActionStorage;
import ru.naumen.data.ErrorStorage;

public class SdngDataSet implements DataSet {
    private ActionStorage actionStorage;
    private ErrorStorage errorStorage;

    public SdngDataSet() {
        actionStorage = new ActionStorage();
        errorStorage = new ErrorStorage();
    }

    public ActionStorage getActionStorage() {
        return actionStorage;
    }

    public ErrorStorage getErrorStorage() {
        return errorStorage;
    }
}
