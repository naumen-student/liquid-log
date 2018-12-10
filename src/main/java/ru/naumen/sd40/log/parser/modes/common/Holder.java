package ru.naumen.sd40.log.parser.modes.common;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.influx.InfluxDAO;


public abstract class Holder<I extends DataSet> implements AutoCloseable {

    protected InfluxDAO influxWrapper;
    protected   String dbName;
    protected BatchPoints points;
    protected boolean needLogging;

    public Holder(Parameters p) {
        this.dbName = p.db;
        influxWrapper = new InfluxDAO(p.host, p.user, p.password);
        points = null;
        this.needLogging = p.needLogging;
        connect();
    }

    abstract public void store(long key, I ds);

    public void connect() {
        influxWrapper.init();
        influxWrapper.connectToDB(dbName);
        points = influxWrapper.startBatchPoints(dbName);
    }

    public void close() throws DBCloseException {
        influxWrapper.writeBatch(points);
    }
}
