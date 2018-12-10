package ru.naumen.sd40.log.parser.modes.sdng.parser;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataParser;
import ru.naumen.sd40.log.parser.modes.sdng.data.ActionDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.data.ErrorDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataSet;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SdngDataParser implements DataParser<SdngDataSet> {

    private static final Pattern WARN_REG_EX = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) WARN");
    private static final Pattern ERROR_REG_EX = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) ERROR");
    private static final Pattern FATAL_REG_EX = Pattern.compile("^\\d+ \\[.+?\\] \\(.+?\\) FATAL");
    private static final Pattern DONE_REG_EX = Pattern.compile("Done\\((\\d+)\\): ?(.*?Action)");

    private static Set<String> EXCLUDED_ACTIONS = new HashSet<>();

    static
    {
        EXCLUDED_ACTIONS.add("EventAction".toLowerCase());
    }


    @Override
    public void parseLine(String line, SdngDataSet ds) {
        parseActionLine(line, ds);
        parseErrorLine(line, ds);
    }

    public void parseActionLine(String line, SdngDataSet ds) {
        ActionDataSet dataSet = ds.getActionDataSet();
        Matcher matcher = DONE_REG_EX.matcher(line);

        if (matcher.find()) {
            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (EXCLUDED_ACTIONS.contains(actionInLowerCase)) {
                return;
            }

            dataSet.getTimes().add(Integer.parseInt(matcher.group(1)));
            if (actionInLowerCase.equals("addobjectaction")) {
                dataSet.incrementAddObjectActions();
            } else if (actionInLowerCase.equals("editobjectaction")) {
                dataSet.incrementEditObjectActions();
            } else if (actionInLowerCase.equals("getcatalogsaction")) {
                dataSet.incrementCatalogsActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+")) {
                dataSet.incrementCommentActions();
            } else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+")) {
                dataSet.incrementListActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+")) {
                dataSet.incrementFormActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+")) {
                dataSet.increaseObjectActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+")) {
                dataSet.incrementSearchActions();
            }

        }
    }

    public void parseErrorLine(String line, SdngDataSet ds) {
        ErrorDataSet dataSet = ds.getErrorDataSet();

        if (WARN_REG_EX.matcher(line).find())
        {
            dataSet.incrementWarnCount();
        }
        if (ERROR_REG_EX.matcher(line).find())
        {
            dataSet.incrementErrorCount();
        }
        if (FATAL_REG_EX.matcher(line).find())
        {
            dataSet.incrementFatalCount();
        }
    }
}
