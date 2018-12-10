package ru.naumen.sd40.log.parser.modes.top.holder;

import ru.naumen.sd40.log.parser.modes.common.Holder;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.top.data.TopDataSet;

public class TopConnector extends Holder<TopDataSet> {
    public TopConnector(Parameters p) {
        super(p);
    }

    @Override
    public void store(long key, TopDataSet ds) {
        if (!ds.isNan())
        {
            influxWrapper.storeData(points, dbName, key, ds);
        }
    }
}
