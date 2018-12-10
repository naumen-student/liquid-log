package ru.naumen.sd40.log.parser.modes.gc.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetController;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.common.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.modes.gc.holder.GCConnector;

@Component
public class GCControllerdataSetFactory implements DataSetControllerFactory<GCDataSet> {
    @Override
    public DataSetController<GCDataSet> create(Parameters p) {
        return new DataSetController<>(new GCConnector(p), new GCDatasetFactory());
    }
}
