package ru.naumen.perfhouse.statdata.influx;

/**
 * Helper class to store 'from' and 'to' dates in UTC for influx queries
 * @author dkolmogortsev
 *
 */
public class InfluxDateRange
{
    private final String from;
    private final String to;

    InfluxDateRange(String from, String to)
    {
        this.from = from;
        this.to = to;
    }

    public String from()
    {
        return from;
    }

    public String to()
    {
        return to;
    }

    @Override
    public String toString()
    {
        return "UTCRange[From:" + from + ",To:" + to + ']';
    }
}
