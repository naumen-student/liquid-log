package ru.naumen.sd40.log.parser.modes.gc.parser;

import org.springframework.stereotype.Component;

import ru.naumen.sd40.log.parser.modes.common.TimeParserFactory;
import ru.naumen.sd40.log.parser.modes.common.TimeParser;


@Component
public class GcTimeParserFactory implements TimeParserFactory {

    private GcTimeParser parser = new GcTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
