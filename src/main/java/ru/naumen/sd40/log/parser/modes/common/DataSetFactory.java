package ru.naumen.sd40.log.parser.modes.common;


import ru.naumen.sd40.log.parser.modes.common.DataSet;

public interface DataSetFactory<I extends DataSet> {
    I create();
}
