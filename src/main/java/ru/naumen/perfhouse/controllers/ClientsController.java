package ru.naumen.perfhouse.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.modes.common.ParseBuilder;

/**
 * Created by dkirpichenkov on 26.10.16.
 */

@Controller
public class ClientsController
{
    private Logger LOG = LoggerFactory.getLogger(ClientsController.class);
    private InfluxDAO influxDAO;
    private Map<String, ParseBuilder> builders;

    @Inject
    public ClientsController(InfluxDAO influxDAO, Map<String, ParseBuilder> builders)
    {
        this.influxDAO = influxDAO;
        this.builders = builders;
    }

    @RequestMapping(path = "/")
    public ModelAndView index()
    {
        List<String> clients = influxDAO.getDbList();
        HashMap<String, Object> clientLast864Links = new HashMap<>();
        HashMap<String, Object> clientLinks = new HashMap<>();
        HashMap<String, Object> clientMonthLinks = new HashMap<>();
        HashMap<String, Object> clientLast2016Links = new HashMap<>();
        HashMap<String, Object> clientPreviousMonthLinks = new HashMap<>();

        DateTime now = DateTime.now();
        DateTime prevMonth = now.minusMonths(1);
        DateTime yesterday = now.minusDays(1);

        clients.forEach(it -> {
            clientLinks.put(it, "/data_types/" + it + "?year=" + yesterday.getYear() + "&month=" + yesterday.getMonthOfYear() + "&day="
                    + yesterday.getDayOfMonth());

            clientMonthLinks.put(it, "/data_types/" + it + "?year=" + now.getYear() + "&month=" + now.getMonthOfYear());
            clientPreviousMonthLinks.put(it,
                    "/data_types/" + it + "?year=" + prevMonth.getYear() + "&month=" + prevMonth.getMonthOfYear());
            clientLast864Links.put(it, "/data_types/" + it + "?count=864");
            clientLast2016Links.put(it, "/data_types/" + it + "?count=2016");
        });


        HashMap<String, Object> model = new HashMap<>();
        model.put("clients", clients);
        model.put("links", clientLinks);
        model.put("monthlinks", clientMonthLinks);
        model.put("last864links", clientLast864Links);
        model.put("last2016links", clientLast2016Links);
        model.put("prevMonthLinks", clientPreviousMonthLinks);
        model.put("parseBuilders", builders.keySet());

        return new ModelAndView("clients", model, HttpStatus.OK);
    }

    @RequestMapping(path = "{client}", method = RequestMethod.POST)
    public void postClientStatFormat1(@PathVariable("client") String client, HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        try
        {
            client = client.replaceAll("-", "_");
            influxDAO.connectToDB(client);
            String data = IOUtils.toString(request.getInputStream(), "UTF-8");
            JSONObject measure = new JSONObject(data);
            influxDAO.storeFromJSon(null, client, measure);
            response.sendError(HttpServletResponse.SC_OK);
        }
        catch (Exception ex)
        {
            LOG.error(ex.toString(), ex);
            throw ex;
        }
    }
}
