package ru.naumen.sd40.log.parser.time;

import java.text.ParseException;
import java.util.Optional;

public interface TimeParser
{
    Optional<Long> parse(String line) throws ParseException;
}