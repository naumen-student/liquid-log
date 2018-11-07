package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.data.ActionStorage;
import ru.naumen.data.ErrorStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SdngDataParser implements DataParser {

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
    public void parseLine(String line, DataSet ds) {
        parseActionLine(line, ds);
        parseErrorLine(line, ds);
    }

    public void parseActionLine(String line, DataSet ds) {
        ActionStorage storage = ds.getActionStorage();
        Matcher matcher = DONE_REG_EX.matcher(line);

        if (matcher.find()) {
            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (EXCLUDED_ACTIONS.contains(actionInLowerCase)) {
                return;
            }

            storage.getTimes().add(Integer.parseInt(matcher.group(1)));
            if (actionInLowerCase.equals("addobjectaction")) {
                storage.incrementAddObjectActions();
            } else if (actionInLowerCase.equals("editobjectaction")) {
                storage.incrementEditObjectActions();
            } else if (actionInLowerCase.equals("getcatalogsaction")) {
                storage.incrementCatalogsActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+")) {
                storage.incrementCommentActions();
            } else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+")) {
                storage.incrementListActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+")) {
                storage.incrementFormActions();
            } else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+")) {
                storage.increaseObjectActions();
            } else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+")) {
                storage.incrementSearchActions();
            }

        }
    }

    public void parseErrorLine(String line, DataSet ds) {
        ErrorStorage storage = ds.getErrorStorage();

        if (WARN_REG_EX.matcher(line).find())
        {
            storage.incrementWarnCount();
        }
        if (ERROR_REG_EX.matcher(line).find())
        {
            storage.incrementErrorCount();
        }
        if (FATAL_REG_EX.matcher(line).find())
        {
            storage.incrementFatalCount();
        }
    }
}
