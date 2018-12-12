package ru.naumen.sd40.log.parser.modes.top.data;

import com.google.common.collect.Lists;
import ru.naumen.sd40.log.parser.modes.common.DataType;
import ru.naumen.sd40.log.parser.utils.GlobalConstants;

import java.util.List;

public enum TopDataType implements DataType {
    TOP(Top.getProps());

    private List<String> props;

    TopDataType(List<String> props) {
        this.props = props;
    }

    @Override
    public List<String> getProps() {
        return props;
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
            return Lists.newArrayList(GlobalConstants.TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
        }
    }
}
