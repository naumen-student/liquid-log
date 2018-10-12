package ru.naumen.sd40.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public abstract class AbstractFileDataParser implements DataParser {

    protected String fileName;
    public AbstractFileDataParser(String fileName) {
        this.fileName = fileName;

    }

    public abstract void configureTimeZone(String timeZone);

    public void parse() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                parseLine(line);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
