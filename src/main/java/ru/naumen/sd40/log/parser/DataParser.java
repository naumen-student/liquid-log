package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.datasetfactory.DataSet;

import java.text.ParseException;

public interface DataParser<I extends DataSet> {


    void parseLine(String line, I ds) throws ParseException;

}
