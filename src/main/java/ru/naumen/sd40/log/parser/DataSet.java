package ru.naumen.sd40.log.parser;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{
    private ActionDoneParser actionsDone;
    private ErrorParser errors;
    private GcDataParser gc;
    private TopData cpuData = new TopData();

    public DataSet()
    {
        actionsDone = new ActionDoneParser();
        errors = new ErrorParser();
        gc = new GcDataParser();
    }



    public ActionDoneParser getActionsDone()
    {
        return actionsDone;
    }

    public ErrorParser getErrors()
    {
        return errors;
    }

    public GcDataParser getGc()
    {
        return gc;
    }

    public TopData cpuData()
    {
        return cpuData;
    }
}
