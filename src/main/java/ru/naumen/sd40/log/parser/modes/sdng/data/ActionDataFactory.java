package ru.naumen.sd40.log.parser.modes.sdng.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.modes.common.DataSetFactory;

@Component
public class ActionDataFactory implements DataSetFactory<ActionDataSet> {
    @Override
    public ActionDataSet create() {
        return new ActionDataSet();
    }
}
