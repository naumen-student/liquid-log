package ru.naumen.sd40.log.parser.modes.sdng.data;

import ru.naumen.sd40.log.parser.modes.common.DataSet;

import java.util.HashMap;

import static ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataType.ResponseTimes.ERRORS;

public class ErrorDataSet implements DataSet {
    private long warnCount;
    private long errorCount;
    private long fatalCount;

    public long getWarnCount()
    {
        return warnCount;
    }

    public void incrementWarnCount() {
        warnCount ++;
    }

    public long getErrorCount()
    {
        return errorCount;
    }

    public void incrementErrorCount() {
        errorCount ++;
    }

    public long getFatalCount()
    {
        return fatalCount;
    }

    public void incrementFatalCount() {
        fatalCount ++;
    }

    @Override
    public HashMap<String, Object> getValues() {
        HashMap<String, Object> values = new HashMap<>();
        values.put(ERRORS, getErrorCount());
        return values;
    }
}
