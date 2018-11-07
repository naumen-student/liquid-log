package ru.naumen.sd40.log.parser;

import ru.naumen.data.ActionStorage;
import ru.naumen.data.ErrorStorage;
import ru.naumen.data.GcStorage;
import ru.naumen.data.TopStorage;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{

    private ActionStorage actionStorage = new ActionStorage();
    private ErrorStorage errorStorage = new ErrorStorage();
    private GcStorage gcStorage = new GcStorage();
    private TopStorage topStorage = new TopStorage();




    public ActionStorage getActionStorage() {
        return actionStorage;
    }

    public ErrorStorage getErrorStorage() {
        return errorStorage;
    }

    public GcStorage getGcStorage() {
        return gcStorage;
    }

    public TopStorage getTopStorage() {
        return topStorage;
    }

}
