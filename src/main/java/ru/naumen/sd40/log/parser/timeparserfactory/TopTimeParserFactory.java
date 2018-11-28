package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TopTimeParser;

@Component
public class TopTimeParserFactory implements TimeParserFactory {

    @Override
    public TimeParser create() {
        return new TopTimeParser();
    }

}
