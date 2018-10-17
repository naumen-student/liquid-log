package ru.naumen.sd40.log.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TimeZone;


public class TopTimeParser implements TimeParser {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm");
    private Pattern timeRegex = Pattern.compile("^_+ (\\S+)");
    private String dataDate;
    private long cachedTime;

    public TopTimeParser(String log) throws IllegalArgumentException {
        
        //Supports these masks in file name: YYYYmmdd, YYY-mm-dd i.e. 20161101, 2016-11-01
        Matcher matcher = Pattern.compile("\\d{8}|\\d{4}-\\d{2}-\\d{2}").matcher(log);
        if (!matcher.find())
        {
            throw new IllegalArgumentException();
        }
        this.dataDate = matcher.group(0).replaceAll("-", "");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

    }

    @Override
    public void configureTimeZone(String timeZone) {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public long parseLine(String line) throws ParseException {
        Matcher matcher = timeRegex.matcher(line);
        if (matcher.find()) {
            cachedTime = sdf.parse(dataDate + matcher.group(1)).getTime();
            return cachedTime;
        }
        return cachedTime;
    }
}
