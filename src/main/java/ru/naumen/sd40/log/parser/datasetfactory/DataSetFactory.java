package ru.naumen.sd40.log.parser.datasetfactory;


public interface DataSetFactory<I extends DataSet> {
    I create();
}
