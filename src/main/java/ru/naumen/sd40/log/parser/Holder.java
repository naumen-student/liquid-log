package ru.naumen.sd40.log.parser;

public interface Holder {

    void store(long key, DataSet ds);

    void close();

    void connect();
}
