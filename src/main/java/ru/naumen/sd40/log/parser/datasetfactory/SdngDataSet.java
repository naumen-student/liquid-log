package ru.naumen.sd40.log.parser.datasetfactory;


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
}
