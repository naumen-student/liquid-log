package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.HashMap;

import org.influxdb.dto.BatchPoints;

import ru.naumen.perfhouse.influx.InfluxDAO;

/**
 * Created by doki on 22.10.16.
 */
public class ParserController {
    /**
     * @param args [0] - sdng.log, [1] - gc.log, [2] - top, [3] - dbName, [4] timezone
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException, InvalidParameterException {

        if (args.length <= 1) {
            System.exit(1);
        }


        String influxDb = args[1].replaceAll("-", "_");
        ;
        String log = args[0];

        HashMap<Long, DataSet> data = new HashMap<>();


        Holder connector = new InfluxConnector(influxDb, System.getProperty("influx.host"),
                System.getProperty("influx.user"), System.getProperty("influx.password"));

        connector.connect();

        DataSetController controller = new DataSetController(connector);

        TimeParser timeParser;
        DataParser dataParser;

        String mode = System.getProperty("parse.mode", "");

        switch (mode) {
            case "sdng":
                timeParser = new SdngTimeParser();
                dataParser = new SdngDataParser();
                break;
            case "gc":
                timeParser = new GcTimeParser();
                dataParser = new GcDataParser();
                break;
            case "top":
                timeParser = new TopTimeParser(log);
                dataParser = new TopDataParser();
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Availiable modes: sdng, gc, top. Requested mode: " + mode);
        }

        if (args.length > 2) {
            timeParser.configureTimeZone(args[2]);
        }

        if (System.getProperty("NoCsv") == null) {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(log))) {
            String line;
            while ((line = br.readLine()) != null) {
                long time = timeParser.parseLine(line);
                if (time == 0) {
                    continue;
                }
                int min5 = 5 * 60 * 1000;
                long count = time / min5;
                long key = count * min5;

                DataSet ds = controller.get(key);
                dataParser.parseLine(line, ds);
            }

        }
        controller.close();
    }
}
