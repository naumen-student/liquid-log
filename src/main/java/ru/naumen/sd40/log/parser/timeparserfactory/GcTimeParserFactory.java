package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;

import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.timeparsers.GcTimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


@Component
@RequestScope
public class GcTimeParserFactory implements TimeParserFactory {

    @Override
    public TimeParser create(String log) {
        return new GcTimeParser();
    }

}
