package ru.naumen.perfhouse.statdata.influx;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Helper to convert dates to UTC date time for influx queries
 * @author dkolmogortsev
 *
 */
public class InfluxDateHelper
{
    /**
     * get {@link InfluxDateRange}
     * builds infux range where 'from' date is based on the passed arguments with midnight time e.g
     * 21/12/2016 00:00:00.000.
     * 'to' is the same date, but time is 23:59:59.999
     * @param year for from
     * @param month for from
     * @param day for from
     * @return influxRange containing 'from' and 'to' dates for influx with pattern yyyy-MM-dd HH:mm:ss.SSS
     */
    public static InfluxDateRange getInfluxRange(int year, int month, int day)
    {
        DateTimeFormatter influxFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTime dtFrom, dtTo;
        dtFrom = new DateTime(year, month, day == 0 ? 1 : day, 0, 0, 0, 0).withZone(DateTimeZone.UTC);
        dtTo = new DateTime(year, month,
                day == 0 ? new YearMonth(year, month).toDateTime(null).dayOfMonth().getMaximumValue() : day, 23, 59, 59,
                999).withZone(DateTimeZone.UTC);
        InfluxDateRange utcRange = new InfluxDateRange(influxFormat.print(dtFrom), influxFormat.print(dtTo));
        return utcRange;
    }

    /**
     * get {@link InfluxDateRange} based on arguments
     * @param from
     * @param to
     * @return influxRange containing 'from' and 'to' dates for influx with pattern yyyy-MM-dd HH:mm:ss.SSS
     */
    public static InfluxDateRange getInfluxRange(String from, String to)
    {
        DateTimeFormatter datePattern = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTimeFormatter influxFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

        long millisFrom = datePattern.parseMillis(from);
        long millisTo = datePattern.parseMillis(to);

        DateTime dateTime = new DateTime(millisFrom);
        DateTime toTime = new DateTime(millisTo).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);
        InfluxDateRange utcRange = new InfluxDateRange(influxFormat.print(dateTime), influxFormat.print(toTime));
        return utcRange;
    }
}
