<%@page import="ru.naumen.sd40.log.parser.utils.GlobalConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.influxdb.dto.QueryResult.Series" %>


<html>

<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/style.css"/>
    <style>
        .col-xs-40 {
        	width: 34%;
        }
        .col-xs-10 {
        	width: 11%;
        }
    }
    </style>
</head>

<body>

<script src="http://code.highcharts.com/highcharts.js"></script>
<div style="margin-left: 20px">
    <br>
    <h1>Performance activity for <%=request.getAttribute("client")%></h1>
    <h4 id="date_range"></h4>
</div>

<div id="chart-container" style="height: 600px"></div>

<script>
<%
    HashMap<String, Number[]> data = (HashMap<String, Number[]>)request.getAttribute("data");
    Number times[] = data.remove(GlobalConstants.TIME);
    Set<String> namesSet = data.keySet();
    String names[] = namesSet.toArray(new String[namesSet.size()]);
    List<Number[]> entries =  new ArrayList<>(data.values());
%>

var names = [];
var times = [];
var entries = [];

<% for(int i=0;i<names.length;i++) {%>
        entries[<%=i%>] = [];
        names[<%=i%>] = "<%=names[i]%>";
<%}%>


<% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
        <% for(int j = 0;j<names.length;j++) {%>
    entries[<%=j%>].push([new Date(<%= times[i] %>), <%= java.lang.Math.round(entries.get(j)[i].intValue()) %>]);
        <%}%>
<% } %>


var resultSeries = [];
    for (var i = 0; i < names.length; i++) {
        resultSeries.push({
            name: names[i],
            data: entries[i],
            turboThreshold: 10000
        })
    }
    
Highcharts.chart('chart-container', {
        chart: {
            zoomType: 'x,y'
        },
        title: {
            text: '<%=request.getAttribute("client")%>'
        },
        tooltip: {
            formatter: function() {
                var index = this.point.index;
                var date =  new Date(times[index]);
                return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                    + '<br/> <b>'+this.series.name+'</b> - '+ this.y + ' times<br/>'
            }
        },
        xAxis: {
            labels:{
                formatter:function(obj){
                    return Highcharts.dateFormat('%a %d %b %H:%M:%S', new Date(times[this.value]));
                }
            },
            reversed: true
        },
        yAxis: {
            title: {
                text: '<%=request.getAttribute("dataTypeName")%>'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                marker: {
                    enabled: false
                }
            }
        },
        series: resultSeries
    });
    document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

</script>


<div class="container scroll-container">
    <table class="table table-fixed header-fixed">
        <thead class="thead-inverse">
            <th class="col-xs-2">Time</th>
            <%for (int i = 0; i < names.length; i++) {%>
            <th class="col-xs-1"><%=names[i]%></th>
            <%}%>
        </thead>
        <tbody>
        <% for(int i=0;i<times.length;i++) {%>
            <tr class="row">
                <td class="col-xs-2" style="text-align:center;">
                    <%= new java.util.Date(times[i].longValue()).toString() %>
                </td>
                <% for(int j=0;j<names.length;j++) {%>
                <td class="col-xs-1">
                    <%= java.lang.Math.round(entries.get(j)[i].intValue()) %>
                </td>
                <%}%>
            </tr>
        <%}%>
        </tbody>
    </table>
</div>

</body>

</html>