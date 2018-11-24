package ru.naumen.perfhouse.influx;

import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.AVARAGE_GC_TIME;
import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.GCTIMES;
import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.MAX_GC_TIME;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.*;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.COUNT;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.ERRORS;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.MAX;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.MEAN;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.PERCENTILE50;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.PERCENTILE95;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.PERCENTILE99;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.PERCENTILE999;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.STDDEV;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_CPU;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_LA;
import static ru.naumen.perfhouse.statdata.Constants.Top.AVG_MEM;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_CPU;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_LA;
import static ru.naumen.perfhouse.statdata.Constants.Top.MAX_MEM;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.datasetfactory.ActionDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.ErrorDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.GCDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSet;

/**
 * Created by doki on 24.10.16.
 */
@Component
public class InfluxDAO
{
    private String influxHost;

    private String user;

    private String password;

    private InfluxDB influx;

    @Autowired
    public InfluxDAO(@Value("${influx.host}") String influxHost, @Value("${influx.user}") String user,
            @Value("${influx.password}") String password)
    {
        this.influxHost = influxHost;
        this.user = user;
        this.password = password;
    }

    public void connectToDB(String dbName)
    {
        influx.createDatabase(dbName);
    }

    @PreDestroy
    public void destroy()
    {
        influx.disableBatch();
    }

    public QueryResult.Series executeQuery(String dbName, String query)
    {
        Query q = new Query(query, dbName);
        QueryResult result = influx.query(q);

        if (result.getResults().get(0).getSeries() == null)
        {
            return null;
        }

        return result.getResults().get(0).getSeries().get(0);
    }

    public List<String> getDbList()
    {
        return influx.describeDatabases();
    }

    @PostConstruct
    public void init()
    {
        influx = InfluxDBFactory.connect(influxHost, user, password);
    }

    public BatchPoints startBatchPoints(String dbName)
    {
        return BatchPoints.database(dbName).build();
    }

    public void storeActionsFromLog(BatchPoints batch, String dbName, long date, ActionDataSet actionDataSet,
                                    ErrorDataSet errorDataSet)
    {
        //@formatter:off
        Builder builder = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(COUNT, actionDataSet.getCount())
                .addField("min", actionDataSet.getMin())
                .addField(MEAN, actionDataSet.getMean())
                .addField(STDDEV, actionDataSet.getStddev())
                .addField(PERCENTILE50, actionDataSet.getPercent50())
                .addField(PERCENTILE95, actionDataSet.getPercent95())
                .addField(PERCENTILE99, actionDataSet.getPercent99())
                .addField(PERCENTILE999, actionDataSet.getPercent999())
                .addField(MAX, actionDataSet.getMax())
                .addField(ERRORS, errorDataSet.getErrorCount())
                .addField(ADD_ACTIONS, actionDataSet.getAddObjectActions())
                .addField(EDIT_ACTIONS, actionDataSet.getEditObjectsActions())
                .addField(LIST_ACTIONS, actionDataSet.geListActions())
                .addField(COMMENT_ACTIONS, actionDataSet.getCommentActions())
                .addField(GET_FORM_ACTIONS, actionDataSet.getFormActions())
                .addField(GET_DT_OBJECT_ACTIONS, actionDataSet.getDtObjectActions())
                .addField(SEARCH_ACTIONS, actionDataSet.getSearchActions())
                .addField(GET_CATALOGS_ACTION, actionDataSet.getGetCatalogsActions());


        //@formatter:on

        Point point = builder.build();

        if (batch != null)
        {
            batch.getPoints().add(point);
        }
        else
        {
            influx.write(dbName, "autogen", point);
        }
    }

    public void storeFromJSon(BatchPoints batch, String dbName, JSONObject data)
    {
        influx.createDatabase(dbName);
        long timestamp = (data.getLong("time"));
        long errors = (data.getLong("errors"));
        double p99 = (data.getDouble("tnn"));
        double p999 = (data.getDouble("tnnn"));
        double p50 = (data.getDouble("tmed"));
        double p95 = (data.getDouble("tn"));
        long count = (data.getLong("tcount"));
        double mean = (data.getDouble("avg"));
        double stddev = (data.getDouble("dev"));
        long max = (data.getLong("tmax"));
        long herrors = data.getLong("hErrors");

        Point measure = Point.measurement("perf").time(timestamp, TimeUnit.MILLISECONDS).addField("count", count)
                .addField("min", 0).addField("mean", mean).addField("stddev", stddev).addField("percent50", p50)
                .addField("percent95", p95).addField("percent99", p99).addField("percent999", p999).addField("max", max)
                .addField("errors", errors).addField("herrors", herrors).build();

        if (batch != null)
        {
            batch.getPoints().add(measure);
        }
        else
        {
            influx.write(dbName, "autogen", measure);
        }
    }

    public void storeGc(BatchPoints batch, String dbName, long date, GCDataSet gcDataSet)
    {
        Point point = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(GCTIMES, gcDataSet.getGcTimes()).addField(AVARAGE_GC_TIME, gcDataSet.getCalculatedAvg())
                .addField(MAX_GC_TIME, gcDataSet.getMaxGcTime()).build();

        if (batch != null)
        {
            batch.getPoints().add(point);
        }
        else
        {
            influx.write(dbName, "autogen", point);
        }
    }

    public void storeTop(BatchPoints batch, String dbName, long date, TopDataSet data)
    {
        Point point = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS)
                .addField(AVG_LA, data.getAvgLa()).addField(AVG_CPU, data.getAvgCpuUsage())
                .addField(AVG_MEM, data.getAvgMemUsage()).addField(MAX_LA, data.getMaxLa())
                .addField(MAX_CPU, data.getMaxCpu()).addField(MAX_MEM, data.getMaxMem()).build();
        if (batch != null)
        {
            batch.getPoints().add(point);
        }
        else
        {
            influx.write(dbName, "autogen", point);
        }
    }

    public void writeBatch(BatchPoints batch)
    {
        influx.write(batch);
    }
}
