package ru.naumen.sd40.log.parser.holders;

import ru.naumen.data.TopStorage;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSet;

public class TopConnector extends Holder<TopDataSet> {
    public TopConnector(Parameters p) {
        super(p);
    }

    @Override
    public void store(long key, TopDataSet ds) {
        TopStorage cpuData = ds.getStorage();
        if (!cpuData.isNan())
        {
            influxWrapper.storeTop(points, dbName, key, cpuData);
        }
    }
}
