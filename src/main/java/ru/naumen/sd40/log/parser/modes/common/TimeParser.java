package ru.naumen.sd40.log.parser.modes.common;

import java.text.ParseException;

public interface TimeParser {

    void configureTimeZone(String timeZone);

    void setFileName(String fileName);

    long parseLine(String line) throws ParseException;
}
