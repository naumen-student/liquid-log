package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GcDataParser implements DataParser {


    private static final Pattern GC_EXECUTION_TIME = Pattern.compile(".*real=(.*)secs.*");


    @Override
    public void parseLine(String line, DataSet dataSet)
    {
        Matcher matcher = GC_EXECUTION_TIME.matcher(line);

        if (matcher.find())
        {
            double val = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            dataSet.getGcStorage().addValue(val);
        }
    }

}
