package ru.naumen.sd40.log.parser.modes.sdng.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetController;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.common.DataSetControllerFactory;
import ru.naumen.sd40.log.parser.modes.sdng.holder.SdngConnector;

@Component
public class SdngControllerDataSetFactory implements DataSetControllerFactory<SdngDataSet> {
    @Override
    public DataSetController<SdngDataSet> create(Parameters p) {
        return new DataSetController<>(new SdngConnector(p), new SdngDataFactory());
    }
}
