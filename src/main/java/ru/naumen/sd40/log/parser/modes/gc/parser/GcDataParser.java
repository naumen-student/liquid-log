package ru.naumen.sd40.log.parser.modes.gc.parser;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataParser;
import ru.naumen.sd40.log.parser.modes.gc.data.GCDataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GcDataParser implements DataParser<GCDataSet> {


    private static final Pattern GC_EXECUTION_TIME = Pattern.compile(".*real=(.*)secs.*");


    @Override
    public void parseLine(String line, GCDataSet dataSet)
    {
        Matcher matcher = GC_EXECUTION_TIME.matcher(line);

        if (matcher.find())
        {
            double val = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            dataSet.addValue(val);
        }
    }

}
