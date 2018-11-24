package ru.naumen.sd40.log.parser.datasetcontrollerfactory;

import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.GCDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.TopDataSetFactory;
import ru.naumen.sd40.log.parser.holders.TopConnector;

public class TopControllerDataSetFactory implements DataSetControllerFactory<TopDataSet> {
    @Override
    public DataSetController<TopDataSet> create(Parameters p) {
        return new DataSetController<>(new TopConnector(p), new TopDataSetFactory());
    }
}
