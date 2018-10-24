package ru.naumen.sd40.log.parser;

public interface Holder extends AutoCloseable {

    void store(long key, DataSet ds);

    void connect();

    void close() throws DBCloseException;
}
