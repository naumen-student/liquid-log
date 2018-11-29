package ru.naumen.sd40.log.parser.datasetcontrollerfactory;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.GCDataSet;
import ru.naumen.sd40.log.parser.datasetfactory.GCDatasetFactory;
import ru.naumen.sd40.log.parser.datasetfactory.SdngDataSet;
import ru.naumen.sd40.log.parser.holders.GCInfluxConnector;

@Component
public class GCControllerdataSetFactory implements DataSetControllerFactory<GCDataSet> {
    @Override
    public DataSetController<GCDataSet> create(Parameters p) {
        return new DataSetController<>(new GCInfluxConnector(p), new GCDatasetFactory());
    }
}
