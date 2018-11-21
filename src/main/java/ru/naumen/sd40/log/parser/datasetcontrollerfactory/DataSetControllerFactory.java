package ru.naumen.sd40.log.parser.datasetcontrollerfactory;

import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.DataSet;

public interface DataSetControllerFactory<I extends DataSet> {
    DataSetController<I> create(Parameters p);
}
