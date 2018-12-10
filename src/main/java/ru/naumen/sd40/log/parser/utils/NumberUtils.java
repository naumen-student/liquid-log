package ru.naumen.sd40.log.parser.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils
{
    public static double roundToTwoPlaces(double value)
    {
        return new BigDecimal(value).setScale(2, RoundingMode.UP).doubleValue();
    }

    public static double getSafeDouble(double value)
    {
        return Double.isNaN(value) ? 0.0d : value;
    }
}
