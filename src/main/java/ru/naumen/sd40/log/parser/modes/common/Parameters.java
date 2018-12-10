package ru.naumen.sd40.log.parser.modes.common;

public class Parameters {
    public final String db;
    public final String host;
    public final String user;
    public final String password;
    public final boolean needLogging;

    public Parameters(String db,String host, String user, String password, boolean needLogging) {
        this.db = db;
        this.host = host;
        this.user = user;
        this.password = password;
        this.needLogging = needLogging;
    }

}
