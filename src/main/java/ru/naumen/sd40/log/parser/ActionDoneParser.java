package ru.naumen.sd40.log.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by doki on 22.10.16.
 */
public class ActionDoneParser
{
    private static Set<String> EXCLUDED_ACTIONS = new HashSet<>();

    static
    {
        EXCLUDED_ACTIONS.add("EventAction".toLowerCase());
    }

    ArrayList<Integer> times = new ArrayList<>();
    double min;
    double mean;
    double stddev;
    double percent50;
    double percent95;
    double percent99;

    double percent999;
    double max;
    long count;
    private int addObjectActions = 0;
    private int editObjectsActions = 0;
    private int getListActions = 0;
    private int commentActions = 0;
    private int getCatalogsActions = 0;

    private int getFormActions = 0;

    private int getDtObjectActions = 0;

    private int searchActions = 0;

    boolean nan = true;

    private HashMap<String, Integer> actions = new HashMap<>();

    Pattern doneRegEx = Pattern.compile("Done\\((\\d+)\\): ?(.*?Action)");

    public void calculate()
    {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        times.forEach(t -> ds.addValue(t));
        min = ds.getMin();
        mean = ds.getMean();
        stddev = ds.getStandardDeviation();
        percent50 = ds.getPercentile(50.0);
        percent95 = ds.getPercentile(95.0);
        percent99 = ds.getPercentile(99.0);
        percent999 = ds.getPercentile(99.9);
        max = ds.getMax();
        count = ds.getN();
        nan = count == 0;
    }

    public int getListActions()
    {
        return getListActions;
    }

    public HashMap<String, Integer> getActionsCounter()
    {
        return actions;
    }

    public int getAddObjectActions()
    {
        return addObjectActions;
    }

    public int getCommentActions()
    {
        return commentActions;
    }

    public long getCount()
    {
        return count;
    }

    public Pattern getDoneRegEx()
    {
        return doneRegEx;
    }

    public int getDtObjectActions()
    {
        return getDtObjectActions;
    }

    public int getEditObjectsActions()
    {
        return editObjectsActions;
    }

    public int getFormActions()
    {
        return getFormActions;
    }

    public int getCatalogsActions()
    {
        return getCatalogsActions;
    }

    public double getMax()
    {
        return max;
    }

    public double getMean()
    {
        return mean;
    }

    public double getMin()
    {
        return min;
    }

    public double getPercent50()
    {
        return percent50;
    }

    public double getPercent95()
    {
        return percent95;
    }

    public double getPercent99()
    {
        return percent99;
    }

    public double getPercent999()
    {
        return percent999;
    }

    public int getSearchActions()
    {
        return searchActions;
    }

    public double getStddev()
    {
        return stddev;
    }

    public ArrayList<Integer> getTimes()
    {
        return times;
    }

    public boolean isNan()
    {
        return nan;
    }

    public void parseLine(String line)
    {
        Matcher matcher = doneRegEx.matcher(line);

        if (matcher.find())
        {
            String actionInLowerCase = matcher.group(2).toLowerCase();
            if (EXCLUDED_ACTIONS.contains(actionInLowerCase))
            {
                return;
            }

            times.add(Integer.parseInt(matcher.group(1)));
            if (actionInLowerCase.equals("addobjectaction"))
            {
                addObjectActions++;
            }
            else if (actionInLowerCase.equals("editobjectaction"))
            {
                editObjectsActions++;
            }
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+comment[a-zA-Z]+"))
            {
                commentActions++;
            }
            else if (!actionInLowerCase.contains("advlist")
                    && actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+List[a-zA-Z]+"))

            {
                getListActions++;
            }
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+Form[a-zA-Z]+"))
            {
                getFormActions++;
            }
            else if (actionInLowerCase.matches("(?i)^([a-zA-Z]+|Get)[a-zA-Z]+DtObject[a-zA-Z]+"))
            {
                getDtObjectActions++;
            }
            else if (actionInLowerCase.matches("(?i)[a-zA-Z]+search[a-zA-Z]+"))
            {
                searchActions++;
            }
            else if (actionInLowerCase.equals("getcatalogsaction"))
            {
                getCatalogsActions++;
            }
        }
    }
}
