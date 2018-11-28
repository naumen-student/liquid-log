package ru.naumen.sd40.log.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.GCControllerdataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.SdngControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.TopControllerDataSetFactory;
import ru.naumen.sd40.log.parser.datasetfactory.DataSet;
import ru.naumen.sd40.log.parser.timeparserfactory.GcTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.SdngTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.TimeParserFactory;
import ru.naumen.sd40.log.parser.timeparserfactory.TopTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

@Component
public class ParseBuilder {

    private SdngDataParser sdngDataParser;
    private GcDataParser gcDataParser;
    private TopDataParser topDataParser;

    private SdngControllerDataSetFactory sdngFactory;
    private GCControllerdataSetFactory gcFactory;
    private TopControllerDataSetFactory topFactory;

    private SdngTimeParserFactory sdngTimeParserFactory;
    private GcTimeParserFactory gcTimeParserFactory;
    private TopTimeParserFactory topTimeParserFactory;

    private Parameters parameters;

    @Autowired
    public ParseBuilder(SdngDataParser sdngDataParser,
                        GcDataParser gcDataParser,
                        TopDataParser topDataParser,
                        SdngControllerDataSetFactory sdngFactory,
                        GCControllerdataSetFactory gcFactory,
                        TopControllerDataSetFactory topFactory,
                        SdngTimeParserFactory sdngTimeParserFactory,
                        GcTimeParserFactory gcTimeParserFactory,
                        TopTimeParserFactory topTimeParserFactory) {

        this.sdngDataParser = sdngDataParser;
        this.gcDataParser = gcDataParser;
        this.topDataParser = topDataParser;

        this.sdngFactory = sdngFactory;
        this.gcFactory = gcFactory;
        this.topFactory = topFactory;

        this.sdngTimeParserFactory = sdngTimeParserFactory;
        this.gcTimeParserFactory = gcTimeParserFactory;
        this.topTimeParserFactory = topTimeParserFactory;
    }

    public ParseBuilder setDbConnection(String db, boolean needLogging) {
        this.parameters = new Parameters(db, System.getProperty("influx.host"),
                System.getProperty("influx.user"), System.getProperty("influx.password"), needLogging);

        return this;
    }



    public void parse(String mode, String file, String timeZone) throws DBCloseException, IOException, ParseException {
        DataParser parser;
        DataSetControllerFactory factory;
        TimeParserFactory timeParserFactory;
        switch (mode) {
            case "sdng":
                parser = sdngDataParser;
                factory = sdngFactory;
                timeParserFactory = sdngTimeParserFactory;

                break;
            case "gc":
                parser = gcDataParser;
                factory = gcFactory;
                timeParserFactory = gcTimeParserFactory;
                break;
            case "top":
                parser = topDataParser;
                factory = topFactory;
                timeParserFactory = topTimeParserFactory;
                break;
            default:
                throw new IllegalArgumentException("Unknown parse mode!");

        }


        TimeParser timeParser = timeParserFactory.create();
        timeParser.configureTimeZone(timeZone);
        timeParser.setFileName(file);

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
                parser.parseLine(line, ds);
            }
        }
    }

}
