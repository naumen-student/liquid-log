package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.timeparsers.SdngTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

@Component
@RequestScope
public class SdngTimeParserFactory implements TimeParserFactory {
    private SdngTimeParser parser = new SdngTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
