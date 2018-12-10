package ru.naumen.sd40.log.parser.modes.sdng.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetFactory;

@Component
public class ErrorDataSetFactory implements DataSetFactory<ErrorDataSet> {
    @Override
    public ErrorDataSet create() {
        return new ErrorDataSet();
    }
}
