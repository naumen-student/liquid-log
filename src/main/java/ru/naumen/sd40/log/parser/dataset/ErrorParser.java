package ru.naumen.sd40.log.parser.dataset;

import java.util.regex.Pattern;

/**
 * Created by doki on 22.10.16.
 */
public class ErrorParser
{
    long warnCount;
    long errorCount;
    long fatalCount;

    Pattern warnRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    Pattern errorRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    Pattern fatalRegEx = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");

    public void parseLine(String line)
    {
        if (warnRegEx.matcher(line).find())
        {
            warnCount++;
        }
        if (errorRegEx.matcher(line).find())
        {
            errorCount++;
        }
        if (fatalRegEx.matcher(line).find())
        {
            fatalCount++;
        }
    }

    public long getWarnCount()
    {
        return warnCount;
    }

    public long getErrorCount()
    {
        return errorCount;
    }

    public long getFatalCount()
    {
        return fatalCount;
    }
}
