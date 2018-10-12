package ru.naumen.sd40.log.parser;

import java.io.IOException;
import java.text.ParseException;

public interface DataParser {

    void configureTimeZone(String timeZone);

    void parse() throws IOException, ParseException;

}
