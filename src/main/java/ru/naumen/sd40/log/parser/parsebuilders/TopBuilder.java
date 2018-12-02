package ru.naumen.sd40.log.parser.parsebuilders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.DataParser;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.TopDataParser;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSet;
import ru.naumen.sd40.log.parser.timeparserfactory.TopTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

@Component("top")
public class TopBuilder implements ParseBuilder {
    private TopDataParser parser;
    private DataSetControllerFactory<TopDataSet> dataSetControllerFactory;
    private TopTimeParserFactory timeParserFactory;

    @Autowired
    public TopBuilder(TopDataParser parser, DataSetControllerFactory<TopDataSet> dataSetControllerFactory,
                       TopTimeParserFactory timeParserFactory) {
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
