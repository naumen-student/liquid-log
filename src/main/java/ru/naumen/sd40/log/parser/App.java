package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

import org.influxdb.dto.BatchPoints;

import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.data.GCDataSetPopulator;
import ru.naumen.sd40.log.parser.data.DataSetPopulator;
import ru.naumen.sd40.log.parser.data.SDNGDataSetPopulator;
import ru.naumen.sd40.log.parser.data.TopDataSetPopulator;
import ru.naumen.sd40.log.parser.dataset.*;
import ru.naumen.sd40.log.parser.time.GCTimeParser;
import ru.naumen.sd40.log.parser.time.TimeParser;
import ru.naumen.sd40.log.parser.time.SDNGTimeParser;
import ru.naumen.sd40.log.parser.time.TopTimeParser;

import static ru.naumen.sd40.log.parser.NumberUtils.floorToClosestMultiple;

/**
 * Created by doki on 22.10.16.
 */
public class App
{
    public static final long TIME_ALIGNMENT = 5 * 60 * 1000;
    public static final int READER_BUFFER_SIZE = 32 * 1024 * 1024;

    private static HashMap<String, Supplier<TimeParser>> registerTimeParsers(String timeZone, String log)
    {
        HashMap<String, Supplier<TimeParser>> timeParsers = new HashMap<>();
        timeParsers.put("sdng", () -> timeZone != null ? new SDNGTimeParser(timeZone) : new SDNGTimeParser());
        timeParsers.put("gc", () -> timeZone != null ? new GCTimeParser(timeZone) : new GCTimeParser());
        timeParsers.put("top", () -> new TopTimeParser(log));
        return timeParsers;
    }

    private static HashMap<String, Supplier<DataSetPopulator>> registerDataSetPopulators()
    {
        HashMap<String, Supplier<DataSetPopulator>> dataSetPopulators = new HashMap<>();
        dataSetPopulators.put("sdng", SDNGDataSetPopulator::new);
        dataSetPopulators.put("gc", GCDataSetPopulator::new);
        dataSetPopulators.put("top", TopDataSetPopulator::new);
        return dataSetPopulators;
    }

    /**
     * 
     * @param args [0] - sdng.log, [1] - gc.log, [2] - top, [3] - dbName, [4] timezone
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException
    {
        String influxDb = null;

        if (args.length > 1)
        {
            influxDb = args[1];
            influxDb = influxDb.replaceAll("-", "_");
        }

        InfluxDAO storage = null;
        if (influxDb != null)
        {
            storage = new InfluxDAO(System.getProperty("influx.host"), System.getProperty("influx.user"),
                    System.getProperty("influx.password"));
            storage.init();
            storage.connectToDB(influxDb);
        }
        InfluxDAO finalStorage = storage;
        String finalInfluxDb = influxDb;
        BatchPoints points = null;

        if (storage != null)
        {
            points = storage.startBatchPoints(influxDb);
        }

        String log = args[0];

        HashMap<Long, DataSet> data = new HashMap<>();


        String mode = System.getProperty("parse.mode", "");

        TimeParser timeParser = registerTimeParsers(args.length > 2 ? args[2] : null, log).get(mode).get();
        DataSetPopulator dataSetPopulator = registerDataSetPopulators().get(mode).get();

        try (BufferedReader br = new BufferedReader(new FileReader(log), READER_BUFFER_SIZE))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                Optional<Long> time = timeParser.parse(line);

                if (time.isPresent())
                {
                    long key = floorToClosestMultiple(time.get(), TIME_ALIGNMENT);
                    dataSetPopulator.populate(line, data.computeIfAbsent(key, k -> new DataSet()));
                }
            }
        }

        if (System.getProperty("NoCsv") == null)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
        BatchPoints finalPoints = points;
        data.forEach((k, set) ->
        {
            ActionDoneParser dones = set.getActionsDone();
            dones.calculate();
            ErrorParser erros = set.getErrors();
            if (System.getProperty("NoCsv") == null)
            {
                System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", k, dones.getCount(),
                        dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                        dones.getPercent99(), dones.getPercent999(), dones.getMax(), erros.getErrorCount()));
            }
            if (!dones.isNan())
            {
                finalStorage.storeActionsFromLog(finalPoints, finalInfluxDb, k, dones, erros);
            }

            GCParser gc = set.getGc();
            if (!gc.isNan())
            {
                finalStorage.storeGc(finalPoints, finalInfluxDb, k, gc);
            }

            TopParser topParser = set.getTop();
            if (!topParser.isNan())
            {
                finalStorage.storeTop(finalPoints, finalInfluxDb, k, topParser);
            }
        });
        storage.writeBatch(points);
    }
}
