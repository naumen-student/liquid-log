package ru.naumen.sd40.log.parser.dataset;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{
    private ActionDoneParser actionsDone;
    private ErrorParser errors;
    private GCParser gc;
    private TopParser top;

    public DataSet()
    {
        actionsDone = new ActionDoneParser();
        errors = new ErrorParser();
        gc = new GCParser();
        top = new TopParser();
    }

    public ActionDoneParser getActionsDone()
    {
        return actionsDone;
    }

    public ErrorParser getErrors()
    {
        return errors;
    }

    public GCParser getGc()
    {
        return gc;
    }

    public TopParser getTop()
    {
        return top;
    }
}
