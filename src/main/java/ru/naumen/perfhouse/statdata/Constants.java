package ru.naumen.perfhouse.statdata;

import java.util.List;

import com.google.common.collect.Lists;

public class Constants
{
    private Constants()
    {

    }

    public static final String MEASUREMENT_NAME = "perf";
    public static final String TIME = "time";

    public static class ResponseTimes
    {

        private ResponseTimes()
        {
        }

        public static final String PERCENTILE50 = "percent50";
        public static final String PERCENTILE95 = "percent95";
        public static final String PERCENTILE99 = "percent99";
        public static final String PERCENTILE999 = "percent999";
        public static final String MAX = "max";
        public static final String COUNT = "count";
        public static final String ERRORS = "errors";
        public static final String MEAN = "mean";
        public static final String STDDEV = "stddev";

        static List<String> getProps()
        {
            return Lists.newArrayList(TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                    PERCENTILE999, MAX);
        }
    }

    public static class GarbageCollection
    {
        private GarbageCollection()
        {
        }

        public static final String GCTIMES = "gcTimes";
        public static final String AVARAGE_GC_TIME = "avgGcTime";
        public static final String MAX_GC_TIME = "maxGcTime";

        static List<String> getProps()
        {
            return Lists.newArrayList(TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
        }
    }

    public static class PerformedActions
    {
        private PerformedActions()
        {

        }

        public static final String ADD_ACTIONS = "addActions";
        public static final String EDIT_ACTIONS = "editActions";
        public static final String LIST_ACTIONS = "listActions";
        public static final String COMMENT_ACTIONS = "commentActions";
        public static final String GET_FORM_ACTIONS = "getFormActions";
        public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
        public static final String SEARCH_ACTIONS = "searchActions";
        public static final String ACTIONS_COUNT = "count";
        public static final String GET_CATALOG_ACTIONS = "getCatalogActions";

        static List<String> getProps()
        {
            return Lists.newArrayList(TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                    GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, SEARCH_ACTIONS, GET_CATALOG_ACTIONS);
        }

    }

    public static class Top
    {
        private Top()
        {

        }

        public static final String AVG_LA = "avgLa";
        public static final String AVG_CPU = "avgCpu";
        public static final String AVG_MEM = "avgMem";
        public static final String MAX_LA = "maxLa";
        public static final String MAX_CPU = "maxCpu";
        public static final String MAX_MEM = "maxMem";

        static List<String> getProps()
        {
            return Lists.newArrayList(TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
        }
    }
}
