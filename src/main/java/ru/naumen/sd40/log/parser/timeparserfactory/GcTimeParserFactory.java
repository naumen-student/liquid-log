package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.timeparsers.GcTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;


@Component
@RequestScope
public class GcTimeParserFactory implements TimeParserFactory {

    private GcTimeParser parser = new GcTimeParser();

    @Override
    public TimeParser create() {
        return parser;
    }

}
