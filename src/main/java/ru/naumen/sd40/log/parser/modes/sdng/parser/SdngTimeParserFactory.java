package ru.naumen.sd40.log.parser.modes.sdng.parser;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.TimeParserFactory;
import ru.naumen.sd40.log.parser.modes.common.TimeParser;

@Component
public class SdngTimeParserFactory implements TimeParserFactory {
    private SdngTimeParser parser = new SdngTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
