package ru.naumen.sd40.log.parser.modes.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Component
public class ParseManager {

    private Map<String, ParseBuilder> builders;

    @Autowired
    public ParseManager(Map<String, ParseBuilder> builders) {
        this.builders = builders;

    }

        public void parse(String mode, String file, String timeZone, String db, boolean needLogging) throws DBCloseException, IOException, ParseException {

            Parameters params = new Parameters(db, System.getProperty("influx.host"),
                    System.getProperty("influx.user"), System.getProperty("influx.password"), needLogging);
            if (!builders.containsKey(mode))
                throw new IllegalArgumentException("Unknown parse mode!");

            ParseBuilder builder = builders.get(mode);

            DataParser parser = builder.getParser();
            TimeParser timeParser = builder.getTimeParser();
            DataSetController controller = builder.getDataSetController(params);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                long time = timeParser.parseLine(line);
                if (time == 0) {
                    continue;
                }
                int min5 = 5 * 60 * 1000;
                long count = time / min5;
                long key = count * min5;

                DataSet ds = controller.get(key);
                parser.parseLine(line, ds);
            }
        }

    }
}
