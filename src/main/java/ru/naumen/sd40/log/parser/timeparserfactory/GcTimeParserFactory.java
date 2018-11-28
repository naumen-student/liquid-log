package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;

import ru.naumen.sd40.log.parser.timeparsers.GcTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;


@Component
public class GcTimeParserFactory implements TimeParserFactory {

    private GcTimeParser parser = new GcTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
