package ru.naumen.sd40.log.parser.parsebuilders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.DataParser;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.SdngDataParser;
import ru.naumen.sd40.log.parser.datasetcontrollerfactory.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.datasetfactory.SdngDataSet;
import ru.naumen.sd40.log.parser.timeparserfactory.SdngTimeParserFactory;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;


@Component("sdng")
public class SdngBuilder implements ParseBuilder {

    private SdngDataParser parser;
    private DataSetControllerFactory dataSetControllerFactory;
    private SdngTimeParserFactory timeParserFactory;

    @Autowired
    public SdngBuilder(SdngDataParser parser, DataSetControllerFactory<SdngDataSet> dataSetControllerFactory,
                       SdngTimeParserFactory timeParserFactory) {
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
