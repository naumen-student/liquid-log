package ru.naumen.perfhouse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.perfhouse.statdata.StatData;
import ru.naumen.perfhouse.statdata.StatDataService;
import ru.naumen.sd40.log.parser.modes.common.DataType;
import ru.naumen.sd40.log.parser.modes.common.ParseBuilder;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HistoryController {

    @Autowired
    private StatDataService service;
    private Map<String, ParseBuilder> parseBuilders;
    private HashMap<String, DataType> dataTypeHashMap = new HashMap<>();

    private static final String NO_HISTORY_VIEW = "no_history";
    private static final String DATA_TYPES = "mode_selector";
    private static final String GRAPHS = "history";

    @Inject
    public HistoryController(Map<String, ParseBuilder> parseBuilders) {
        this.parseBuilders = parseBuilders;
        for (ParseBuilder builder: this.parseBuilders.values()) {
            DataType[] dataTypes = builder.getDataTypes();
            for (DataType dataType : dataTypes)
                dataTypeHashMap.put(dataType.toString().toLowerCase(), dataType);
        }

    }

    @RequestMapping(path = "/history/{client}/{dataType}/{year}/{month}/{day}")
    public ModelAndView byDay(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                     @PathVariable(name = "year", required = false) int year,
                                     @PathVariable(name = "month", required = false) int month,
                                     @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, dataTypeHashMap.get(dataType), year, month, day, GRAPHS, false);
    }

    @RequestMapping(path = "/history/{client}/{dataType}/{year}/{month}")
    public ModelAndView byMonth(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                       @PathVariable(name = "year", required = false) int year,
                                       @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, dataTypeHashMap.get(dataType), year, month, 0, GRAPHS, true);
    }

    @RequestMapping(path = "/history/{client}/{dataType}")
    public ModelAndView last864(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                  @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndView(client, dataTypeHashMap.get(dataType), count, GRAPHS);

    }

    @RequestMapping(path = "/data_types/{client}")
    public ModelAndView custom_init(@PathVariable("client") String client,
                                    @RequestParam(value = "year", required = false) Integer year,
                                    @RequestParam(value = "month", required = false) Integer month,
                                    @RequestParam(value = "day", required = false) Integer day,
                                    @RequestParam(value = "from", required = false) String from,
                                    @RequestParam(value = "to", required = false) String to,
                                    @RequestParam(value = "maxResults", defaultValue = "1000") int count) {
        Map<String, Object> model = new HashMap<>();
        model.put("client", client);
        if (from != null && to != null) {
            model.put("custom", true);
            model.put("from", from);
            model.put("to", to);
            model.put("maxResults", count);
        }
        else {
            model.put("year", year);
            model.put("month", month);
            model.put("day", day);
        }

        model.put("dataTypes", dataTypeHashMap.keySet());
        return new ModelAndView(DATA_TYPES, model, HttpStatus.OK);

    }

    @RequestMapping(path = "/history/{client}/custom/{dataType}")
    public ModelAndView custom(@PathVariable("client") String client, @RequestParam("dataType") String dataType,
                               @RequestParam("from") String from,
                               @RequestParam("to") String to,
                               @RequestParam("maxResults") int maxResults) throws ParseException
    {
        return getDataAndViewCustom(client, dataTypeHashMap.get(dataType), from, to, maxResults, GRAPHS);
    }


    private ModelAndView getDataAndView(String client, DataType dataType, int count, String viewName)
            throws ParseException
    {
        StatData data = service.getData(client, dataType, count);
        if (data == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("data", data.asModel());
        model.put("client", client);
        model.put("dataTypes", dataTypeHashMap.keySet());
        model.put("dataTypeName", dataType);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
                                              String viewName, boolean compress) throws ParseException
    {
        StatData data = service.getDataDate(client, type, year, month, day);

        if (data == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        data = compress ? service.compress(data, 3 * 60 * 24 / 5) : data;
        Map<String, Object> model = new HashMap<>();
        model.put("data", data.asModel());
        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);
        model.put("dataTypes", dataTypeHashMap.keySet());
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewCustom(String client, DataType dataType, String from, String to, int maxResults,
                                              String viewName) throws ParseException
    {
        StatData data = service.getDataCustom(client, dataType, from, to);
        if (data == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        data = service.compress(data, maxResults);
        Map<String, Object> model = new HashMap<>();
        model.put("data", data.asModel());
        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);
        model.put("dataTypes", dataTypeHashMap.keySet());
        model.put("dataTypeName", dataType);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }


}
