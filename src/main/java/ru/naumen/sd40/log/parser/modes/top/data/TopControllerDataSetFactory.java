package ru.naumen.sd40.log.parser.modes.top.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetController;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.common.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.modes.top.holder.TopConnector;

@Component
public class TopControllerDataSetFactory implements DataSetControllerFactory<TopDataSet> {
    @Override
    public DataSetController<TopDataSet> create(Parameters p) {
        return new DataSetController<>(new TopConnector(p), new TopDataSetFactory());
    }
}
