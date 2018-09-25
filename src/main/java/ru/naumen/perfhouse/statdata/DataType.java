package ru.naumen.perfhouse.statdata;

import java.util.List;

import ru.naumen.perfhouse.statdata.Constants.Top;
import ru.naumen.perfhouse.statdata.Constants.GarbageCollection;
import ru.naumen.perfhouse.statdata.Constants.PerformedActions;
import ru.naumen.perfhouse.statdata.Constants.ResponseTimes;

public enum DataType
{
    //@formatter:off
    RESPONSE(ResponseTimes.getProps()),
    GARBAGE_COLLECTION(GarbageCollection.getProps()),
    ACTIONS(PerformedActions.getProps()),
    TOP(Top.getProps());
    //@formtatter:on
    
    private List<String> properties;

    DataType(List<String> properties)
    {
        this.properties = properties;
    }

    List<String> getTypeProperties()
    {
        return this.properties;
    }
}
