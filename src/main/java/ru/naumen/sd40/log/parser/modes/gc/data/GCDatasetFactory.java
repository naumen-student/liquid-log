package ru.naumen.sd40.log.parser.modes.gc.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetFactory;

@Component
public class GCDatasetFactory implements DataSetFactory<GCDataSet> {
    @Override
    public GCDataSet create() {
        return new GCDataSet();
    }
}
