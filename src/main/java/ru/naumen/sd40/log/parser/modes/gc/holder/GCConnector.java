package ru.naumen.sd40.log.parser.modes.gc.holder;

import ru.naumen.sd40.log.parser.modes.common.Holder;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.gc.data.GCDataSet;

public class GCConnector extends Holder<GCDataSet> {

    public GCConnector(Parameters p ) {
        super(p);
    }

    @Override
    public void store(long key, GCDataSet ds) {
        if (!ds.isNan())
        {
            influxWrapper.storeData(points, dbName, key, ds);
        }
    }
}
