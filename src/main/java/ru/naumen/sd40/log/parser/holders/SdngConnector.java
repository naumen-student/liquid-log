package ru.naumen.sd40.log.parser.holders;

import ru.naumen.data.ActionStorage;
import ru.naumen.data.ErrorStorage;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.SdngDataSet;

public class SdngConnector extends Holder<SdngDataSet> {
    public SdngConnector(Parameters p) {
        super(p);
    }

    @Override
    public void store(long key, SdngDataSet ds) {
        ActionStorage dones = ds.getActionStorage();
        dones.calculate();
        ErrorStorage errors = ds.getErrorStorage();
        if (needLogging)
        {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", key, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), errors.getErrorCount()));
        }
        if (!dones.isNan())
        {
            influxWrapper.storeActionsFromLog(points, dbName, key, dones, errors);
        }
    }
}
