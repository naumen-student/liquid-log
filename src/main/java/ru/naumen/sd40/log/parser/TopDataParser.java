package ru.naumen.sd40.log.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopDataParser implements DataParser {

    private Pattern cpuAndMemPattren = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");

    @Override
    public void parseLine(String line, DataSet ds) throws ParseException {
        //get la

        Matcher la = Pattern.compile(".*load average:(.*)").matcher(line);
        if (la.find())
        {
            ds.cpuData().addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
            return;
        }
        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattren.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            ds.cpuData().addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            ds.cpuData().addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
            return;
        }
    }
}
