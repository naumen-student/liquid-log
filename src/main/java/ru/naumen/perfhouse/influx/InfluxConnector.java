package ru.naumen.perfhouse.influx;

import ru.naumen.DBConnector;
import ru.naumen.sd40.log.parser.dataset.*;

public class InfluxConnector implements DBConnector
{
    private InfluxDAO storage;
    private String dbName;

    public InfluxConnector(String dbName, String host, String user, String password)
    {
        storage = new InfluxDAO(host, user, password);
        storage.init();
        storage.connectToDB(dbName);
        this.dbName = dbName;
    }

    @Override
    public void store(long key, DataSet dataSet)
    {
        ActionDoneParser dones = dataSet.getActionsDone();
        dones.calculate();
        ErrorParser errors = dataSet.getErrors();
        if (System.getProperty("NoCsv") == null)
        {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", key, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(), errors.getErrorCount()));
        }
        if (!dones.isNan())
        {
            storage.storeActionsFromLog(null, dbName, key, dones, errors);
        }

        GCParser gc = dataSet.getGc();
        if (!gc.isNan())
        {
            storage.storeGc(null, dbName, key, gc);
        }

        TopParser topParser = dataSet.getTop();
        if (!topParser.isNan())
        {
            storage.storeTop(null, dbName, key, topParser);
        }
    }
}
