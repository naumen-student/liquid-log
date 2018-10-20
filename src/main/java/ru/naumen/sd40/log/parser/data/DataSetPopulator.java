package ru.naumen.sd40.log.parser.data;

import ru.naumen.sd40.log.parser.dataset.DataSet;

import java.text.ParseException;

public interface DataSetPopulator
{
    void populate(String line, DataSet dataSet) throws ParseException;
}
