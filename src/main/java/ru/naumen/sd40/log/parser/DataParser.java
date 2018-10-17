package ru.naumen.sd40.log.parser;

import java.text.ParseException;

public interface DataParser {


    void parseLine(String line, DataSet ds) throws ParseException;

}
