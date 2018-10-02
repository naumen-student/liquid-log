package ru.naumen.perfhouse.statdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.influxdb.dto.QueryResult.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;

import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.perfhouse.statdata.influx.InfluxDateHelper;
import ru.naumen.perfhouse.statdata.influx.InfluxDateRange;

/**
 * Component for getting data from influx
 * @author dkolmogortsev
 *
 */
@Component
public class StatDataService
{
    private static class NumberComparator<T extends Number> implements Comparator<T>
    {

        @SuppressWarnings("unchecked")
        @Override
        public int compare(T o1, T o2)
        {
            //Standard java classes are comparable
            if (o1 instanceof Comparable && o1.getClass().equals(o2.getClass()))
            {
                return ((Comparable<T>)o1).compareTo(o2);
            }
            return -1;
        }

    }

    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    private InfluxDAO influxdao;

    private final NumberComparator<Number> comparator = new NumberComparator<>();

    /**
     * Compress data to required size
     * @param toCompress data to compress
     * @param compression target data size
     * @return compressed data
     */
    public StatData compress(StatData toCompress, int compression)
    {
        int dataSize = toCompress.getDataSize();
        if (dataSize <= compression)
        {
            return toCompress;
        }
        StatData result = new StatData(compression);

        long step = dataSize / compression;
        int position = 0;
        for (int i = 0; i < dataSize; i++)
        {
            if (i > 0 && i % step == 0)
            {
                position++;

                if (position == compression)
                {
                    position--;
                }

                if (position * step >= dataSize)
                {
                    position--;
                }
            }

            int timePosition = (int)(step * position);

            if (timePosition >= dataSize)
            {
                timePosition = (int)(step * (position - 1));
            }

            Number dataAt = toCompress.getDataAt(Constants.TIME, timePosition);
            result.setDataAt(Constants.TIME, dataAt, position);

            Set<String> dataNames = toCompress.getDataProperties();
            dataNames.remove(Constants.TIME);
            for (String name : dataNames)
            {
                Number resultData = result.getDataAt(name, position);
                Number dataAtCompressed = toCompress.getDataAt(name, i);
                if (resultData == null || comparator.compare(resultData, dataAtCompressed) == -1)
                {
                    result.setDataAt(name, dataAtCompressed, position);
                }
            }
        }

        return result;
    }

    public StatData getData(String client, DataType dataType, int maxResults) throws ParseException
    {
        Series result = influxdao.executeQuery(client, prepareQuery(dataType, maxResults));
        if (result == null)
        {
            return null;
        }

        StatData data = createData(result);
        return data;
    }

    public StatData getDataCustom(String client, DataType type, String from, String to) throws ParseException
    {
        InfluxDateRange utcRange = InfluxDateHelper.getInfluxRange(from, to);
        String template = "SELECT %s FROM %s WHERE %s ORDER BY %s DESC";
        String time = Constants.TIME;

        String where = time + ">='" + utcRange.from() + "' and " + time + "<='" + utcRange.to() + "'";

        String query = String.format(template, Joiner.on(',').join(type.getTypeProperties()),
                Constants.MEASUREMENT_NAME, where, time);

        Series result = influxdao.executeQuery(client, query);
        if (result == null)
        {
            return null;
        }
        return createData(result);
    }

    public StatData getDataDate(String client, DataType dataType, int year, int month, int day) throws ParseException
    {
        String q = prepareQueryDate(dataType, year, month, day);
        Series result = influxdao.executeQuery(client, q);
        if (result == null)
        {
            return null;
        }
        return createData(result);
    }

    private StatData createData(Series result) throws ParseException
    {
        SimpleDateFormat parser = new SimpleDateFormat(pattern);
        parser.setTimeZone(TimeZone.getTimeZone("GMT"));

        StatData data = new StatData(result.getValues().size());
        List<String> columns = result.getColumns();
        int index = 0;
        for (List<Object> row : result.getValues())
        {
            long time = parser.parse((String)row.get(0)).getTime();
            data.setDataAt(Constants.TIME, time, index);
            for (int i = 1; i < row.size(); i++)
            {
                Number value = (Number)row.get(i);
                data.setDataAt(columns.get(i), value == null ? 0 : value, index);
            }
            index++;
        }
        return data;
    }

    private String prepareQuery(DataType type, int count)
    {
        String qTemp = "SELECT %s from %s ORDER BY %s DESC LIMIT %s";
        return String.format(qTemp, Joiner.on(',').join(type.getTypeProperties()), Constants.MEASUREMENT_NAME,
                Constants.TIME, count);
    }

    private String prepareQueryDate(DataType dataType, int year, int month, int day)
    {
        String template = "SELECT %s from %s WHERE %s ORDER BY %s DESC";

        return String.format(template, Joiner.on(',').join(dataType.getTypeProperties()), Constants.MEASUREMENT_NAME,
                prepareWhere(year, month, day), Constants.TIME);
    }

    private String prepareWhere(int year, int month, int day)
    {
        InfluxDateRange utcRange = InfluxDateHelper.getInfluxRange(year, month, day);
        return Constants.TIME + ">='" + utcRange.from() + "'and " + Constants.TIME + "<='" + utcRange.to() + "'";

    }
}
