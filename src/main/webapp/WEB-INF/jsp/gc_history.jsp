<%@page import="ru.naumen.perfhouse.statdata.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
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
</head>

<body>

<script src="http://code.highcharts.com/highcharts.js"></script>
<%
    Number times[] = (Number[])request.getAttribute(Constants.TIME);
    Number gcTimes[]=  (Number[])request.getAttribute(Constants.GarbageCollection.GCTIMES);
    Number gcAvg[] = (Number[])request.getAttribute(Constants.GarbageCollection.AVARAGE_GC_TIME);
    Number gcMax[] = (Number[])request.getAttribute(Constants.GarbageCollection.MAX_GC_TIME);
    
  //Prepare links
  	String path="";
  	String custom = "";
  	if(request.getAttribute("custom") == null){
  	Object year = request.getAttribute("year");
  	Object month = request.getAttribute("month");
  	Object day = request.getAttribute("day");
      
      String countParam = (String)request.getParameter("count");
      
  	String params = "";
  	String datePath = "";

  	StringBuilder sb = new StringBuilder();


  	if(countParam != null){
      	params = sb.append("?count=").append(countParam).toString();
  	}else{
      	sb.append('/').append(year).append('/').append(month);
      	if(!day.toString().equals("0")){
          	sb.append('/').append(day);
      	}
      	datePath = sb.toString();
  	}
  	path = datePath + params;
  	}
  	else{
  	    custom = "/custom";
  	    Object from = request.getAttribute("from");
  	  	Object to = request.getAttribute("to");
  	  	Object maxResults = request.getAttribute("maxResults");
  	  	
  	  	StringBuilder sb = new StringBuilder();
  	  	path = sb.append("?from=").append(from).append("&to=").append(to).append("&maxResults=").append(maxResults).toString();
  	}
      
    
%>

<div class="container">
	<br>
    <h1>Performance data for "${client}"</h1>
    <h3><a class="btn btn-success btn-lg" href="/">Client list</a></h3>
    <h4 id="date_range"></h4>
    <p>
        Feel free to hide/show specific data by clicking on chart's legend
    </p>
    <ul class="nav nav-pills">
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %><%=path%>">Responses</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/actions<%=path%>">Performed actions</a></li>
		<li class="nav-item"><a class="nav-link active">Garbage Collection</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/top<%=path%>">Top data</a></li>
	</ul>
</div>

<!-- Gc chart -->
<div class="container" id="gc">
<div id="gc-chart-container" style="height: 600px"></div>
<div class="scroll-container">
	<table class="table table-fixed header-fixed">
        <thead class="thead-inverse">
            <th class="col-xs-3">Time</th>
            <th class="col-xs-3">Number of performed GC</th>
            <th class="col-xs-3">Avarage GC time, ms</th>
            <th class="col-xs-3">Max GC Time,ms</th>
        </thead>
        <tbody >
            <% for(int i=0;i<times.length;i++) {%>
                <tr class="row">
                    <td class="col-xs-3" style="text-align:center;">
                       <%= new java.util.Date(times[i].longValue()).toString() %>
                    </td>
                    <td class="col-xs-3" >
                        <%= gcTimes[i].intValue() %>
                    </td>
                    <td class="col-xs-3">
                        <%= gcAvg[i] %>
                    </td>
                    <td class="col-xs-3">
                        <%= gcMax[i] %>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

</div>
<script type="text/javascript">
var times = [];
var gcTimes = [];
var gcAvg = [];
var gcMaxTime = [];

<% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
    gcTimes.push([new Date(<%= times[i] %>), <%= Math.round(gcTimes[i].doubleValue()) %>]);
    gcAvg.push([new Date(<%= times[i] %>), <%= gcAvg[i] %>]);
    gcMaxTime.push([new Date(<%= times[i] %>), <%= gcMax[i] %>]);
<% } %>

document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

if(localStorage.getItem('gcTimes')==null){
    localStorage.setItem('gcTimes', 'false');
}
if(localStorage.getItem('avgGcTime')==null){
    localStorage.setItem('avgGcTime', 'false');
}
if(localStorage.getItem('maxGcTime')==null){
    localStorage.setItem('maxGcTime', 'true');
}


var gcTimesvisible = localStorage.getItem('gcTimes')==='true';
var avgGcTimevisible = localStorage.getItem('avgGcTime')==='true';
var maxGcTimevisible = localStorage.getItem('maxGcTime')==='true';

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

var myChart = Highcharts.chart('gc-chart-container', {
        chart: {
                zoomType: 'x,y'
            },

        title: {
            text: 'Garbage collection'
        },

        tooltip: {
            formatter: function() {
                            var index = this.point.index;
                            var date =  new Date(times[index]);
                            return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                            + '<br/> <b>'+this.series.name+'</b> '+ this.y + ' '+this.series.options.unit+ '<br/>'
                        }
        },

        xAxis: {
            labels:{
                formatter:function(obj){
//                        var index = this.point.index;
//                        var date =  new Date(times[index]);
                        return Highcharts.dateFormat('%a %d %b %H:%M:%S', new Date(times[this.value]));
                    }
                },
                reversed: true
        },

        yAxis: {
            title: {
                text: 'GC'
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
                },
                events: {
                    legendItemClick: function(event) {
                        var series = this.yAxis.series;
                        seriesLen = series.length;

                        if(event.target.index==0){
                            localStorage.setItem('gcTimes', !series[0].visible);
                        }
                        if(event.target.index==1){
                            localStorage.setItem('avgGcTime', !series[1].visible);
                        }
                        if(event.target.index==2){
                            localStorage.setItem('maxGcTime', !series[2].visible);
                        }
                    }
                }
            }
        },
        series: [{
            name: 'GC Performed',
            data: gcTimes,
            visible: gcTimesvisible,
            unit: 'times',
            turboThreshold: 10000
        }, {
            name: 'Average GC Time',
            data: gcAvg,
            visible: avgGcTimevisible,
            unit: 'ms',
            turboThreshold: 10000
            
        }, {
            name: 'Max GC Time',
            data: gcMaxTime,
            visible: maxGcTimevisible,
            unit: 'ms',
            turboThreshold: 10000
        }]
});

</script>

</body>

</html>