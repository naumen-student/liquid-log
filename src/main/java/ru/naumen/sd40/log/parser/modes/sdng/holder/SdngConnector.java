package ru.naumen.sd40.log.parser.modes.sdng.holder;

import ru.naumen.sd40.log.parser.modes.common.Holder;
import ru.naumen.sd40.log.parser.modes.common.Parameters;
import ru.naumen.sd40.log.parser.modes.sdng.data.ActionDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.data.ErrorDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataSet;

public class SdngConnector extends Holder<SdngDataSet> {
    public SdngConnector(Parameters p) {
        super(p);
    }

    @Override
    public void store(long key, SdngDataSet ds) {
        ActionDataSet dones = ds.getActionDataSet();
        dones.calculate();
        ErrorDataSet errors = ds.getErrorDataSet();
        if (needLogging)
        {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", key, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), errors.getErrorCount()));
        }
        if (!dones.isNan())
        {
            influxWrapper.storeData(points, dbName, key, ds);
        }
    }
}
