package ru.naumen.sd40.log.parser;

import java.text.ParseException;

public class SdngDataParser implements DataParser {
    @Override
    public void parseLine(String line, DataSet ds) {
        ds.getErrors().parseLine(line, ds);
        ds.getActionsDone().parseLine(line, ds);
    }
}
