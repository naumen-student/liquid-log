package ru.naumen.perfhouse.controllers;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.naumen.perfhouse.statdata.DataType;
import ru.naumen.perfhouse.statdata.StatData;
import ru.naumen.perfhouse.statdata.StatDataService;

/**
 * Created by doki on 23.10.16.
 */
@Controller
public class HistoryController
{

    @Autowired
    StatDataService service;

    private static final String NO_HISTORY_VIEW = "no_history";
    private static final String HISTORY_VIEW = "history";
    private static final String ACTIONS_VIEW = "history_actions";
    private static final String GC_VIEW = "gc_history";
    private static final String CPU_VIEW = "history_top";

    @RequestMapping(path = "/history/{client}/{year}/{month}/{day}")
    public ModelAndView indexByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.RESPONSE, year, month, day, HISTORY_VIEW);
    }

    @RequestMapping(path = "/history/{client}/actions/{year}/{month}/{day}")
    public ModelAndView actionsByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.ACTIONS, year, month, day, ACTIONS_VIEW);
    }

    @RequestMapping(path = "/history/{client}/gc/{year}/{month}/{day}")
    public ModelAndView gcByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.GARBAGE_COLLECTION, year, month, day, GC_VIEW);
    }

    @RequestMapping(path = "/history/{client}/top/{year}/{month}/{day}")
    public ModelAndView topByDay(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month,
            @PathVariable(name = "day", required = false) int day) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.TOP, year, month, day, CPU_VIEW);
    }

    @RequestMapping(path = "/history/{client}/{year}/{month}")
    public ModelAndView indexByMonth(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.RESPONSE, year, month, 0, HISTORY_VIEW, true);
    }

    @RequestMapping(path = "/history/{client}/actions/{year}/{month}")
    public ModelAndView actionsByMonth(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.ACTIONS, year, month, 0, ACTIONS_VIEW, true);
    }

    @RequestMapping(path = "/history/{client}/gc/{year}/{month}")
    public ModelAndView gcByMonth(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.GARBAGE_COLLECTION, year, month, 0, GC_VIEW, true);
    }

    @RequestMapping(path = "/history/{client}/top/{year}/{month}")
    public ModelAndView topByMonth(@PathVariable("client") String client,
            @PathVariable(name = "year", required = false) int year,
            @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        return getDataAndViewByDate(client, DataType.TOP, year, month, 0, CPU_VIEW, true);
    }

    @RequestMapping(path = "/history/{client}")
    public ModelAndView indexLast864(@PathVariable("client") String client,
            @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        ru.naumen.perfhouse.statdata.StatData d = service.getData(client, DataType.RESPONSE, count);

        if (d == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        Map<String, Object> model = new HashMap<>(d.asModel());
        model.put("client", client);

        return new ModelAndView("history", model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/gc")
    public ModelAndView gcLast864(@PathVariable("client") String client,
            @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndView(client, DataType.GARBAGE_COLLECTION, count, GC_VIEW);

    }

    private ModelAndView getDataAndView(String client, DataType dataType, int count, String viewName)
            throws ParseException
    {
        ru.naumen.perfhouse.statdata.StatData data = service.getData(client, dataType, count);
        if (data == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);

        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
            String viewName) throws ParseException
    {
        return getDataAndViewByDate(client, type, year, month, day, viewName, false);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
            String viewName, boolean compress) throws ParseException
    {
        ru.naumen.perfhouse.statdata.StatData dataDate = service.getDataDate(client, type, year, month, day);
        if (dataDate == null)
        {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        dataDate = compress ? service.compress(dataDate, 3 * 60 * 24 / 5) : dataDate;
        Map<String, Object> model = new HashMap<>(dataDate.asModel());
        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);
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
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    @RequestMapping(path = "/history/{client}/actions")
    public ModelAndView actionsLast864(@PathVariable("client") String client,
            @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndView(client, DataType.ACTIONS, count, ACTIONS_VIEW);
    }

    @RequestMapping(path = "/history/{client}/top")
    public ModelAndView topLast864(@PathVariable("client") String client,
            @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        return getDataAndView(client, DataType.TOP, count, CPU_VIEW);
    }

    @RequestMapping(path = "/history/{client}/custom")
    public ModelAndView customIndex(@PathVariable("client") String client, @RequestParam("from") String from,
            @RequestParam("to") String to, @RequestParam("maxResults") int maxResults) throws ParseException
    {
        return getDataAndViewCustom(client, DataType.RESPONSE, from, to, maxResults, HISTORY_VIEW);
    }

    @RequestMapping(path = "/history/{client}/custom/actions")
    public ModelAndView customActions(@PathVariable("client") String client, @RequestParam("from") String from,
            @RequestParam("to") String to, @RequestParam("maxResults") int count) throws ParseException
    {
        return getDataAndViewCustom(client, DataType.ACTIONS, from, to, count, ACTIONS_VIEW);
    }

    @RequestMapping(path = "/history/{client}/custom/gc")
    public ModelAndView customGc(@PathVariable("client") String client, @RequestParam("from") String from,
            @RequestParam("to") String to, @RequestParam("maxResults") int count) throws ParseException
    {
        return getDataAndViewCustom(client, DataType.GARBAGE_COLLECTION, from, to, count, GC_VIEW);
    }

    @RequestMapping(path = "/history/{client}/custom/top")
    public ModelAndView customTop(@PathVariable("client") String client, @RequestParam("from") String from,
            @RequestParam("to") String to, @RequestParam("maxResults") int count) throws ParseException
    {
        return getDataAndViewCustom(client, DataType.TOP, from, to, count, CPU_VIEW);
    }

}
