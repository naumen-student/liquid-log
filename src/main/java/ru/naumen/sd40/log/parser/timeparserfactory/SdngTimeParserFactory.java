package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.timeparsers.SdngTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

@Component
public class SdngTimeParserFactory implements TimeParserFactory {
    private SdngTimeParser parser = new SdngTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
