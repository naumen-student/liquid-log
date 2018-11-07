package ru.naumen.sd40.log.parser;

import org.influxdb.dto.BatchPoints;
import org.springframework.beans.factory.annotation.Autowired;
import ru.naumen.data.ActionStorage;
import ru.naumen.data.ErrorStorage;
import ru.naumen.data.GcStorage;
import ru.naumen.data.TopStorage;
import ru.naumen.perfhouse.influx.InfluxDAO;

public class InfluxConnector implements Holder {

    @Autowired
    private InfluxDAO influxWrapper;
    private  String dbName;
    private BatchPoints points;
    private boolean needLogging;

    public  InfluxConnector(String dbName, String host, String user, String password, boolean needLogging) {
        this.dbName = dbName;
        influxWrapper = new InfluxDAO(host, user, password);
        points = null;
        this.needLogging = needLogging;
    }

    public void connect() {
        influxWrapper.init();
        influxWrapper.connectToDB(dbName);
        points = influxWrapper.startBatchPoints(dbName);
    }

    public  void close() {
        influxWrapper.writeBatch(points);
    }

    @Override
    public void store(long key, DataSet ds) {
        ActionStorage dones = ds.getActionStorage();
        dones.calculate();
        ErrorStorage erros = ds.getErrorStorage();
        if (needLogging) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", key, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(),
                    erros.getErrorCount()));
        }
        if (!dones.isNan()) {
            influxWrapper.storeActionsFromLog(points, dbName, key, dones, erros);
        }

        GcStorage gc = ds.getGcStorage();
        if (!gc.isNan()) {
            influxWrapper.storeGc(points, dbName, key, gc);
        }

        TopStorage cpuData = ds.getTopStorage();
        if (!cpuData.isNan()) {
            influxWrapper.storeTop(points, dbName, key, cpuData);
        }
    }
}
