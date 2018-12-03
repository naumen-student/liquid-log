package ru.naumen.sd40.log.parser.parsebuilders;

import ru.naumen.sd40.log.parser.DataParser;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

public interface ParseBuilder {

    DataParser getParser();
    DataSetController getDataSetController(Parameters p);
    TimeParser getTimeParser();

}
