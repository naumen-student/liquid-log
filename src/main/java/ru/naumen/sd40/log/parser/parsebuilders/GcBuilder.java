package ru.naumen.sd40.log.parser.parsebuilders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.DataParser;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.GcDataParser;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetfactory.GCDataSet;
import ru.naumen.sd40.log.parser.timeparserfactory.GcTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

@Component("gc")
public class GcBuilder implements ParseBuilder {

    private GcDataParser parser;
    private DataSetControllerFactory<GCDataSet> dataSetControllerFactory;
    private GcTimeParserFactory timeParserFactory;

    @Autowired
    public GcBuilder(GcDataParser parser, DataSetControllerFactory<GCDataSet> dataSetControllerFactory,
                      GcTimeParserFactory timeParserFactory) {
        this.parser = parser;
        this.dataSetControllerFactory = dataSetControllerFactory;
        this.timeParserFactory = timeParserFactory;
    }

    @Override
    public DataParser getParser() {
        return parser;
    }

    @Override
    public DataSetController getDataSetController(Parameters p) {
        return dataSetControllerFactory.create(p);
    }

    @Override
    public TimeParser getTimeParser() {
        return timeParserFactory.create();
    }
}
