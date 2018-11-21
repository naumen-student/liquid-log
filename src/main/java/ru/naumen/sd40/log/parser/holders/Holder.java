package ru.naumen.sd40.log.parser.holders;

import org.influxdb.dto.BatchPoints;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.DBCloseException;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.DataSet;

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
