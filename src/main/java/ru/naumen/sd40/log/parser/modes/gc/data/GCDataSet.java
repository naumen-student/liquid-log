package ru.naumen.sd40.log.parser.modes.gc.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.modes.common.DataSet;


import java.util.HashMap;

import static ru.naumen.sd40.log.parser.modes.gc.data.GcDataType.GarbageCollection.AVARAGE_GC_TIME;
import static ru.naumen.sd40.log.parser.modes.gc.data.GcDataType.GarbageCollection.GCTIMES;
import static ru.naumen.sd40.log.parser.modes.gc.data.GcDataType.GarbageCollection.MAX_GC_TIME;
import static ru.naumen.sd40.log.parser.utils.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.utils.NumberUtils.roundToTwoPlaces;

public class GCDataSet implements DataSet {
    private DescriptiveStatistics ds = new DescriptiveStatistics();

    public double getCalculatedAvg()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMean()));
    }

    public long getGcTimes()
    {
        return ds.getN();
    }

    public double getMaxGcTime()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMax()));
    }

    public boolean isNan()
    {
        return getGcTimes() == 0;
    }

    public void addValue(double value){
        ds.addValue(value);
    }

    @Override
    public HashMap<String, Object> getValues() {
        HashMap<String, Object> values = new HashMap<>();
        if (!isNan()){
            values.put(GCTIMES, getGcTimes());
            values.put(AVARAGE_GC_TIME, getCalculatedAvg());
            values.put(MAX_GC_TIME, getMaxGcTime());
        }
        return values;
    }
}
