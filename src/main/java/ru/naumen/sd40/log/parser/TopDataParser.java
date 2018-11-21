package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSet;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TopDataParser implements DataParser<TopDataSet> {

    private static final Pattern CPU_AND_MEM_PATTREN = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");

    @Override
    public void parseLine(String line, TopDataSet ds) throws ParseException {
        //get la

        Matcher la = Pattern.compile(".*load average:(.*)").matcher(line);
        if (la.find())
        {
            ds.getStorage().addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
            return;
        }
        //get cpu and mem
        Matcher cpuAndMemMatcher = CPU_AND_MEM_PATTREN.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            ds.getStorage().addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            ds.getStorage().addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }
}
