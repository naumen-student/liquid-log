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
<%
    Number times[] = (Number[])request.getAttribute(Constants.TIME);
    Number avgLa[]=  (Number[])request.getAttribute(Constants.Top.AVG_LA);
    Number avgCpu[]=  (Number[])request.getAttribute(Constants.Top.AVG_CPU);
    Number avgMem[]=  (Number[])request.getAttribute(Constants.Top.AVG_MEM);
    Number maxLa[]=  (Number[])request.getAttribute(Constants.Top.MAX_LA);
    Number maxCpu[]=  (Number[])request.getAttribute(Constants.Top.MAX_CPU);
    Number maxMem[]=  (Number[])request.getAttribute(Constants.Top.MAX_MEM);
    
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
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %><%=path %>">Responses</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/actions<%=path %>">Performed actions</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/gc<%=path %>">Garbage Collection</a></li>
		<li class="nav-item"><a class="nav-link active" >Top data</a></li>
	</ul>
</div>

<div class="container">
<div id="cpu-chart-container" style="height: 600px"></div>
<div >
	<table class="table table-fixed header-fixed">
        <thead class="thead-inverse">
            <th class="col-xs-40">Time</th>
            <th class="col-xs-10">Avg LA</th>
            <th class="col-xs-10">Avg CPU,%</th>
            <th class="col-xs-10">Avg MEM,%</th>
            <th class="col-xs-10">Max LA</th>
            <th class="col-xs-10">Max CPU, %</th>
            <th class="col-xs-10">Max MEM, %</th>
        </thead>
        <tbody>
            <% for(int i=0;i<times.length;i++) {%>
                <tr class="row">
                    <td class="col-xs-40" style="text-align:center;">
                       <%= new java.util.Date(times[i].longValue()).toString() %>
                    </td>
                    <td class="col-xs-10" >
                        <%= avgLa[i] %>
                    </td>
                    <td class="col-xs-10">
                        <%= avgCpu[i] %>
                    </td>
                    <td class="col-xs-10">
                        <%= avgMem[i] %>
                    </td>
                    <td class="col-xs-10">
                        <%= maxLa[i] %>
                    </td>
                    <td class="col-xs-10">
                        <%= maxCpu[i] %>
                    </td>
                    <td class="col-xs-10">
                        <%= maxMem[i] %>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

</div>
<script type="text/javascript">
var times = [];
var avgLa = [];
var avgCpu = [];
var avgMem = [];
var maxLa = [];
var maxCpu = [];
var maxMem = [];

<% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
    avgLa.push([new Date(<%= times[i] %>), <%= avgLa[i] %>]);
    avgCpu.push([new Date(<%= times[i] %>), <%= avgCpu[i] %>]);
    avgMem.push([new Date(<%= times[i] %>), <%= avgMem[i] %>]);
    maxLa.push([new Date(<%= times[i] %>), <%= maxLa[i] %>]);
    maxCpu.push([new Date(<%= times[i] %>), <%= maxCpu[i] %>]);
    maxMem.push([new Date(<%= times[i] %>), <%= maxMem[i] %>]);
<% } %>

document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

if(localStorage.getItem('avgLa')==null){
    localStorage.setItem('avgLa', 'false');
}
if(localStorage.getItem('avgCpu')==null){
    localStorage.setItem('avgCpu', 'false');
}
if(localStorage.getItem('avgMem')==null){
    localStorage.setItem('avgMem', 'true');
}
if(localStorage.getItem('maxLa')==null){
    localStorage.setItem('maxLa', 'true');
}
if(localStorage.getItem('maxCpu')==null){
    localStorage.setItem('maxCpu', 'true');
}
if(localStorage.getItem('maxMem')==null){
    localStorage.setItem('maxMem', 'true');
}


var avgLaVisible = localStorage.getItem('avgLa')==='true';
var avgCpuVisible = localStorage.getItem('avgCpu')==='true';
var avgMemVisible = localStorage.getItem('avgMem')==='true';
var maxLaVisible = localStorage.getItem('maxLa')==='true';
var maxCpuVisible = localStorage.getItem('maxCpu')==='true';
var maxMemVisible = localStorage.getItem('maxMem')==='true';

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

var myChart = Highcharts.chart('cpu-chart-container', {
        chart: {
                zoomType: 'x,y'
            },

        title: {
            text: 'Top data'
        },

        tooltip: {
            formatter: function() {
                            var index = this.point.index;
                            var date =  new Date(times[index]);
                            return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                            + '<br/> <b>'+this.series.name+'</b> '+ this.y + this.series.options.unit + '<br/>'
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
                text: 'Top data'
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
                            localStorage.setItem('avgLa', !series[0].visible);
                        }
                        if(event.target.index==1){
                            localStorage.setItem('avgCpu', !series[1].visible);
                        }
                        if(event.target.index==2){
                            localStorage.setItem('avgMem', !series[2].visible);
                        }
                        if(event.target.index==3){
                            localStorage.setItem('maxLa', !series[3].visible);
                        }
                        if(event.target.index==4){
                            localStorage.setItem('maxCpu', !series[4].visible);
                        }
                        if(event.target.index==5){
                            localStorage.setItem('maxMem', !series[5].visible);
                        }
                    }
                }
            }
        },
        series: [{
            name: 'Average LA',
            data: avgLa,
            visible: avgLaVisible,
            unit: '',
            turboThreshold: 10000
        }, {
            name: 'Avarage CPU usage',
            data: avgCpu,
            visible: avgCpuVisible,
            unit: '%',
            turboThreshold: 10000
        }, {
            name: 'Average MEM usage',
            data: avgMem,
            visible: avgMemVisible,
            unit: '%',
            turboThreshold: 10000
        }, {
            name: 'Max LA',
            data: maxLa,
            visible: maxLaVisible,
            unit: '',
            turboThreshold: 10000
        }, {
            name: 'Max CPU usage',
            data: maxCpu,
            visible: maxCpuVisible,
            unit: '%',
            turboThreshold: 10000
        }, {
            name: 'Max MEM Usage',
            data: maxMem,
            visible: maxMemVisible,
            unit: '%',
            turboThreshold: 10000
        }]
});

</script>

</body>

</html>