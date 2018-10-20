package ru.naumen.sd40.log.parser.data;

import ru.naumen.sd40.log.parser.dataset.DataSet;

public class GCDataSetPopulator implements DataSetPopulator
{
    @Override
    public void populate(String line, DataSet dataSet) {
        dataSet.getGc().parseLine(line);
    }
}
