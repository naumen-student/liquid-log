package ru.naumen.perfhouse.statdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Data structure, representing data acquired from influx
 */
public class StatData
{
    private final HashMap<String, Number[]> struct = new HashMap<>();

    private final int dataSize;

    StatData(int dataSize)
    {
        this.dataSize = dataSize;
    }

    public Map<String, Number[]> asModel()
    {
        return struct;
    }

    public Number getDataAt(String dataKey, int position)
    {
        return struct.computeIfAbsent(dataKey, f -> new Number[dataSize])[position];
    }

    public Set<String> getDataProperties()
    {
        return new HashSet<String>(struct.keySet());
    }

    public int getDataSize()
    {
        return dataSize;
    }

    public void setDataAt(String dataKey, Number data, int dataPosition)
    {
        struct.computeIfAbsent(dataKey, f -> new Number[dataSize])[dataPosition] = data;
    }

    @Override
    public String toString()
    {
        return "Stat data properties: " + getDataProperties();
    }

}
