package ru.naumen.sd40.log.parser.modes.top.parser;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.TimeParserFactory;
import ru.naumen.sd40.log.parser.modes.common.TimeParser;

@Component
public class TopTimeParserFactory implements TimeParserFactory {

    @Override
    public TimeParser create() {
        return new TopTimeParser();
    }

}
