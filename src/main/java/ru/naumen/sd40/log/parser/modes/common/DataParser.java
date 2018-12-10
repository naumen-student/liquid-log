package ru.naumen.sd40.log.parser.modes.common;

import ru.naumen.sd40.log.parser.modes.common.DataSet;

import java.text.ParseException;

public interface DataParser<I extends DataSet> {


    void parseLine(String line, I ds) throws ParseException;

}
