package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TopTimeParser;

@Component
@RequestScope
public class TopTimeParserFactory implements TimeParserFactory {
    private TopTimeParser parser = new TopTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
