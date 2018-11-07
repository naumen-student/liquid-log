package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;


public class ParseBuilder {

    private TimeParser timeParser;
    private DataParser dataParser;
    private Holder connector;

    private String fileName;

    public ParseBuilder() {

    }

    public ParseBuilder setDbConnection(String db, boolean needLogging) {
        connector = new InfluxConnector(db, System.getProperty("influx.host"),
                System.getProperty("influx.user"), System.getProperty("influx.password"), needLogging);
        connector.connect();
        return this;
    }

    public ParseBuilder setParseMode(String mode, String file, String timeZone) {

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
                timeParser = new TopTimeParser(file);
                dataParser = new TopDataParser();
                break;
            default:
                throw new IllegalArgumentException("Unknown parse mode!");

        }
        fileName = file;
        timeParser.configureTimeZone(timeZone);
        return this;
    }



    public void parse() throws DBCloseException, IOException, ParseException {
        if (connector == null)
            throw new IllegalStateException("DB connection not set");
        if (timeParser == null)
            throw  new IllegalStateException("Parsers are not set");


        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             DataSetController controller = new DataSetController(connector)
        ) {
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
    }

}
