package ru.naumen.sd40.log.parser.modes.common;

public interface DataSetControllerFactory<I extends DataSet> {
    DataSetController<I> create(Parameters p);
}
