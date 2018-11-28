package ru.naumen.sd40.log.parser.datasetcontrollerfactory;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.DataSetController;
import ru.naumen.sd40.log.parser.Parameters;
import ru.naumen.sd40.log.parser.datasetfactory.SdngDataFactory;
import ru.naumen.sd40.log.parser.datasetfactory.SdngDataSet;
import ru.naumen.sd40.log.parser.holders.SdngConnector;

@Component
public class SdngControllerDataSetFactory implements DataSetControllerFactory<SdngDataSet> {
    @Override
    public DataSetController<SdngDataSet> create(Parameters p) {
        return new DataSetController<>(new SdngConnector(p), new SdngDataFactory());
    }
}
