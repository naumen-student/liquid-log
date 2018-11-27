package ru.naumen.sd40.log.parser.timeparserfactory;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.naumen.sd40.log.parser.timeparsers.TimeParser;
import ru.naumen.sd40.log.parser.timeparsers.TopTimeParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
@RequestScope
public class TopTimeParserFactory implements TimeParserFactory {
    @Override
    public TimeParser create(String log) {
        return new TopTimeParser(log);
    }

}
