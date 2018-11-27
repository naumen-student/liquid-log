package ru.naumen.sd40.log.parser.timeparsers;

import java.text.ParseException;

public interface TimeParser {

    void configureTimeZone(String timeZone);

    long parseLine(String line) throws ParseException;
}
