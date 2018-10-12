package ru.naumen.sd40.log.parser;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{
    private ActionDoneParser actionsDone;
    private ErrorParser errors;
    private GCParser gc;
    private TopData cpuData = new TopData();

    public DataSet()
    {
        actionsDone = new ActionDoneParser();
        errors = new ErrorParser();
        gc = new GCParser();
    }


    public  void parse(String line, ParsingMethod method) {
        if (method ==  ParsingMethod.GC)
            parseGcLine(line);
        if (method == ParsingMethod.SDNG)
            parseLine(line);

    }
    private void parseLine(String line)
    {
        errors.parseLine(line);
        actionsDone.parseLine(line);
    }

    private void parseGcLine(String line)
    {
        gc.parseLine(line);
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

    public TopData cpuData()
    {
        return cpuData;
    }
}
