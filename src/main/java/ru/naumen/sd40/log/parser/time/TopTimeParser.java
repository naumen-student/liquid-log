package ru.naumen.sd40.log.parser.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.naumen.sd40.log.parser.App.TIME_ALIGNMENT;
import static ru.naumen.sd40.log.parser.NumberUtils.floorToClosestMultiple;

public class TopTimeParser implements TimeParser
{
    private String dataDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm");
    private Pattern timeRegex = Pattern.compile("^_+ (\\S+)");
    private long lastParsedTime;

    public TopTimeParser(String file) throws IllegalArgumentException
    {
        Matcher matcher = Pattern.compile("\\d{8}|\\d{4}-\\d{2}-\\d{2}").matcher(file);
        if (!matcher.find()) {
            throw new IllegalArgumentException();
        }
        this.dataDate = matcher.group(0).replaceAll("-", "");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    @Override
    public Optional<Long> parse(String line) throws ParseException
    {
        Matcher matcher = timeRegex.matcher(line);
        if (matcher.find()) {
            lastParsedTime = floorToClosestMultiple(sdf.parse(dataDate + matcher.group(1)).getTime(), TIME_ALIGNMENT);
            return Optional.empty();
        }
        return Optional.of(lastParsedTime);
    }
}
