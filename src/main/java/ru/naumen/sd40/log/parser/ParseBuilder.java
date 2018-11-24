package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.GCControllerdataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.SdngControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.TopControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetfactory.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;


public class ParseBuilder {

    private TimeParser timeParser;
    private DataParser dataParser;


    private String fileName;
    private DataSetControllerFactory factory;
    private Parameters parameters;
    private DataSetController controller;

    public ParseBuilder() {

    }

    public ParseBuilder setDbConnection(String db, boolean needLogging) {
        this.parameters = new Parameters(db, System.getProperty("influx.host"),
                System.getProperty("influx.user"), System.getProperty("influx.password"), needLogging);

        return this;
    }

    public ParseBuilder setParseMode(String mode, String file, String timeZone) {

        switch (mode) {
            case "sdng":
                timeParser = new SdngTimeParser();
                dataParser = new SdngDataParser();
                factory = new SdngControllerDataSetFactory();

                break;
            case "gc":
                timeParser = new GcTimeParser();
                dataParser = new GcDataParser();
                factory = new GCControllerdataSetFactory();
                break;
            case "top":
                timeParser = new TopTimeParser(file);
                dataParser = new TopDataParser();
                factory = new TopControllerDataSetFactory();
                break;
            default:
                throw new IllegalArgumentException("Unknown parse mode!");

        }
        controller = factory.create(parameters);
        fileName = file;
        timeParser.configureTimeZone(timeZone);
        return this;
    }



    public void parse() throws DBCloseException, IOException, ParseException {
        if (controller == null)
            throw new IllegalStateException("DB connection not set");
        if (timeParser == null)
            throw  new IllegalStateException("Parsers are not set");


        try (BufferedReader br = new BufferedReader(new FileReader(fileName))
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
