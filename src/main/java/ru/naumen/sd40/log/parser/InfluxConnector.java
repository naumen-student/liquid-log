package ru.naumen.sd40.log.parser;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.influx.InfluxDAO;

public class InfluxConnector implements Holder {

    private InfluxDAO influxWrapper;
    private  String dbName;
    private BatchPoints points;

    public  InfluxConnector(String dbName, String host, String user, String password) {
        this.dbName = dbName;
        influxWrapper = new InfluxDAO(host, user, password);
        points = null;

    }

    public void connect() {
        influxWrapper.init();
        influxWrapper.connectToDB(dbName);
        points =influxWrapper.startBatchPoints(dbName);
    }

    public  void close() {
        influxWrapper.writeBatch(points);
    }

    @Override
    public void store(long key, DataSet ds) {
        ActionDoneParser dones = ds.getActionsDone();
        dones.calculate();
        ErrorParser erros = ds.getErrors();
        if (System.getProperty("NoCsv") == null) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", key, dones.getCount(),
                    dones.getMin(), dones.getMean(), dones.getStddev(), dones.getPercent50(), dones.getPercent95(),
                    dones.getPercent99(), dones.getPercent999(), dones.getMax(),
                    erros.getErrorCount()));
        }
        if (!dones.isNan()) {
            influxWrapper.storeActionsFromLog(points, dbName, key, dones, erros);
        }

        GcDataParser gc = ds.getGc();
        if (!gc.isNan()) {
            influxWrapper.storeGc(points, dbName, key, gc);
        }

        TopData cpuData = ds.cpuData();
        if (!cpuData.isNan()) {
            influxWrapper.storeTop(points, dbName, key, cpuData);
        }
    }
}
