package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.GCControllerdataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.SdngControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.TopControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetfactory.DataSet;
import ru.naumen.sd40.log.parser.timeparserfactory.GcTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.SdngTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.TimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.TopTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparsers.GcTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.SdngTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TopTimeParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;


public class ParseBuilder {


    private DataParser dataParser;

    private DataSetControllerFactory factory;
    private TimeParserFactory timeParserFactory;
    private Parameters parameters;

    public ParseBuilder() {

    }

    public ParseBuilder setDbConnection(String db, boolean needLogging) {
        this.parameters = new Parameters(db, System.getProperty("influx.host"),
                System.getProperty("influx.user"), System.getProperty("influx.password"), needLogging);

        return this;
    }

    public ParseBuilder setParseMode(String mode) {

        switch (mode) {
            case "sdng":
                dataParser = new SdngDataParser();
                factory = new SdngControllerDataSetFactory();
                timeParserFactory = new SdngTimeParserFactory();

                break;
            case "gc":
                dataParser = new GcDataParser();
                factory = new GCControllerdataSetFactory();
                timeParserFactory = new GcTimeParserFactory();
                break;
            case "top":
                dataParser = new TopDataParser();
                factory = new TopControllerDataSetFactory();
                timeParserFactory = new TopTimeParserFactory();
                break;
            default:
                throw new IllegalArgumentException("Unknown parse mode!");

        }

        return this;
    }



    public void parse(String file, String timeZone) throws DBCloseException, IOException, ParseException {
        if (factory == null)
            throw new IllegalStateException("DB connection not set");
        if (timeParserFactory == null)
            throw  new IllegalStateException("Parsers are not set");

        TimeParser timeParser = timeParserFactory.create(file);
        timeParser.configureTimeZone(timeZone);
        DataSetController controller = factory.create(parameters);

        try (BufferedReader br = new BufferedReader(new FileReader(file))
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
