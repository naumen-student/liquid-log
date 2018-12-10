package ru.naumen.sd40.log.parser.modes.sdng.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetFactory;

@Component
public class SdngDataFactory implements DataSetFactory<SdngDataSet> {
    @Override
    public SdngDataSet create() {
        return new SdngDataSet();
    }
}
