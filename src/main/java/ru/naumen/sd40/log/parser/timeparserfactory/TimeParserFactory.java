package ru.naumen.sd40.log.parser.timeparserfactory;

import ru.naumen.sd40.log.parser.timeparsers.TimeParser;

public interface TimeParserFactory {
    TimeParser create();
}
