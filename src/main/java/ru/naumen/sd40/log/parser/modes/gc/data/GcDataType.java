package ru.naumen.sd40.log.parser.modes.gc.data;

import com.google.common.collect.Lists;
import ru.naumen.sd40.log.parser.modes.common.DataType;
import ru.naumen.sd40.log.parser.utils.GlobalConstants;

import java.util.List;

public enum GcDataType implements DataType {

    GARBAGE_COLLECTION(GarbageCollection.getProps());

    private List<String> props;

    GcDataType(List<String> props) {
        this.props = props;
    }

    @Override
    public List<String> getProps() {
        return props;
    }

    public static class GarbageCollection {

        private GarbageCollection() { }
        public static final String GCTIMES = "gcTimes";
        public static final String AVARAGE_GC_TIME = "avgGcTime";
        public static final String MAX_GC_TIME = "maxGcTime";

        static List<String> getProps()
        {
            return Lists.newArrayList(GlobalConstants.TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
        }
    }
}
