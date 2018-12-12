package ru.naumen.sd40.log.parser.modes.sdng.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.*;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataType;


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

    @Override
    public DataType[] getDataTypes() {
        return SdngDataType.values();
    }
}
