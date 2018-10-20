package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

import ru.naumen.perfhouse.influx.InfluxConnector;
import ru.naumen.sd40.log.parser.data.GCDataSetPopulator;
import ru.naumen.sd40.log.parser.data.DataSetPopulator;
import ru.naumen.sd40.log.parser.data.SDNGDataSetPopulator;
import ru.naumen.sd40.log.parser.data.TopDataSetPopulator;
import ru.naumen.sd40.log.parser.time.GCTimeParser;
import ru.naumen.sd40.log.parser.time.TimeParser;
import ru.naumen.sd40.log.parser.time.SDNGTimeParser;
import ru.naumen.sd40.log.parser.time.TopTimeParser;

import static ru.naumen.sd40.log.parser.NumberUtils.floorToClosestMultiple;

/**
 * Created by doki on 22.10.16.
 */
public class LogUploader
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
        if (args.length <= 1)
        {
            System.out.println("Not enough arguments for database initialization");
        }
        String dbName = args[1].replaceAll("-", "_");
        DataSetUploader uploader = new DataSetUploader(new InfluxConnector(
                dbName,
                System.getProperty("influx.host"),
                System.getProperty("influx.user"),
                System.getProperty("influx.password")));

        String log = args[0];
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
                    dataSetPopulator.populate(line, uploader.get(key));
                }
            }
        } catch (DataSetUploader.AlreadyProcessedKeyException e) {
            System.out.println("Log file has incorrect format: log lines are not ordered by time.");
        }
        uploader.close();
    }
}
