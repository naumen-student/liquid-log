package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GcDataParser implements DataParser {


    private Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");


    @Override
    public void parseLine(String line, DataSet dataSet)
    {
        Matcher matcher = gcExecutionTime.matcher(line);

        if (matcher.find())
        {
            double val = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            dataSet.getGcStorage().addValue(val);
        }
    }

}
