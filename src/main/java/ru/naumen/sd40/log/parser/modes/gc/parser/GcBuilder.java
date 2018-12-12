package ru.naumen.sd40.log.parser.modes.gc.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.*;
import ru.naumen.sd40.log.parser.modes.gc.data.GCDataSet;
import ru.naumen.sd40.log.parser.modes.gc.data.GcDataType;

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

    @Override
    public DataType[] getDataTypes() {
        return GcDataType.values();
    }
}
