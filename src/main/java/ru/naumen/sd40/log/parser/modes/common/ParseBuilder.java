package ru.naumen.sd40.log.parser.modes.common;

public interface ParseBuilder {

    DataParser getParser();
    DataSetController getDataSetController(Parameters p);
    TimeParser getTimeParser();
    DataType [] getDataTypes();

}
