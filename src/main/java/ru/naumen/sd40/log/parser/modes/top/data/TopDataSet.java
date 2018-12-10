package ru.naumen.sd40.log.parser.modes.top.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.modes.common.DataSet;

import java.util.HashMap;

import static ru.naumen.sd40.log.parser.modes.top.data.TopDataType.Top.*;
import static ru.naumen.sd40.log.parser.utils.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.utils.NumberUtils.roundToTwoPlaces;

public class TopDataSet implements DataSet {
    private DescriptiveStatistics laStat = new DescriptiveStatistics();
    private DescriptiveStatistics cpuStat = new DescriptiveStatistics();
    private DescriptiveStatistics memStat = new DescriptiveStatistics();

    public void addLa(double la)
    {
        laStat.addValue(la);
    }

    public void addCpu(double cpu)
    {
        cpuStat.addValue(cpu);
    }

    public void addMem(double mem)
    {
        memStat.addValue(mem);
    }

    public boolean isNan()
    {
        return laStat.getN() == 0 && cpuStat.getN() == 0 && memStat.getN() == 0;
    }

    public double getAvgLa()
    {
        return roundToTwoPlaces(getSafeDouble(laStat.getMean()));
    }

    public double getAvgCpuUsage()
    {
        return roundToTwoPlaces(getSafeDouble(cpuStat.getMean()));
    }

    public double getAvgMemUsage()
    {
        return roundToTwoPlaces(getSafeDouble(memStat.getMean()));
    }

    public double getMaxLa()
    {
        return roundToTwoPlaces(getSafeDouble(laStat.getMax()));
    }

    public double getMaxCpu()
    {
        return roundToTwoPlaces(getSafeDouble(cpuStat.getMax()));
    }

    public double getMaxMem()
    {
        return roundToTwoPlaces(getSafeDouble(memStat.getMax()));
    }

    @Override
    public HashMap<String, Object> getValues() {
        HashMap<String, Object> values = new HashMap<>();
        if (!isNan()) {
            values.put(AVG_LA, getAvgLa());
            values.put(AVG_CPU, getAvgCpuUsage());
            values.put(AVG_MEM, getAvgMemUsage());
            values.put(MAX_LA, getMaxLa());
            values.put(MAX_CPU, getMaxCpu());
            values.put(MAX_MEM, getMaxMem());
        }
        return values;
    }
}
