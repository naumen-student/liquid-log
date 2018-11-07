package ru.naumen.data;

public class ErrorStorage implements DataStorage {

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
}
