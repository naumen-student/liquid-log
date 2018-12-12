package ru.naumen.sd40.log.parser.modes.sdng.data;

import com.google.common.collect.Lists;
import ru.naumen.sd40.log.parser.modes.common.DataType;
import ru.naumen.sd40.log.parser.utils.GlobalConstants;

import java.util.List;

public enum  SdngDataType implements DataType {
    RESPONSE(ResponseTimes.getProps()),
    ACTIONS(PerformedActions.getProps());

    private List<String> props;

    SdngDataType(List<String> props) {
        this.props = props;
    }

    @Override
    public List<String> getProps() {
        return props;
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
        public static final String GET_CATALOGS_ACTION = "getCatalogsAction";

        static List<String> getProps()
        {
            return Lists.newArrayList(GlobalConstants.TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                    GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, SEARCH_ACTIONS, GET_CATALOGS_ACTION);
        }

    }

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
        public static final String MIN = "min";
        public static final String COUNT = "count";
        public static final String ERRORS = "errors";
        public static final String MEAN = "mean";
        public static final String STDDEV = "stddev";

        static List<String> getProps()
        {
            return Lists.newArrayList(GlobalConstants.TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                    PERCENTILE999, MAX, MIN);
        }
    }

}
