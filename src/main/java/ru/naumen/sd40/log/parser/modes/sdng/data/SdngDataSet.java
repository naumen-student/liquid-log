package ru.naumen.sd40.log.parser.modes.sdng.data;


import ru.naumen.sd40.log.parser.modes.common.DataSet;

import java.util.HashMap;

public class SdngDataSet implements DataSet {
    private ActionDataSet actionDataSet;
    private ErrorDataSet errorDataSet;

    public SdngDataSet() {
        actionDataSet = new ActionDataSet();
        errorDataSet = new ErrorDataSet();
    }

    public ActionDataSet getActionDataSet() {
        return actionDataSet;
    }

    public ErrorDataSet getErrorDataSet() {
        return errorDataSet;
    }

    @Override
    public HashMap<String, Object> getValues() {
        HashMap<String, Object> values = new HashMap<>();
        values.putAll(actionDataSet.getValues());
        values.putAll(errorDataSet.getValues());
        return values;
    }
}
