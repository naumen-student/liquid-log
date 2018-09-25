<%@page import="ru.naumen.perfhouse.statdata.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.influxdb.dto.QueryResult.Series" %>
<%@ page import="ru.naumen.perfhouse.statdata.Constants.ResponseTimes" %>

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
    Number p50[] = (Number[])request.getAttribute(Constants.ResponseTimes.PERCENTILE50);
    Number p95[] = (Number[])request.getAttribute(Constants.ResponseTimes.PERCENTILE95);
    Number p99[] = (Number[])request.getAttribute(Constants.ResponseTimes.PERCENTILE99);
    Number p999[] = (Number[])request.getAttribute(Constants.ResponseTimes.PERCENTILE999);
    Number p100[] = (Number[])request.getAttribute(Constants.ResponseTimes.MAX);
    Number count[]= (Number[])request.getAttribute(Constants.ResponseTimes.COUNT);
    Number errors[]= (Number[])request.getAttribute(Constants.ResponseTimes.ERRORS);
    Number mean[]= (Number[])request.getAttribute(Constants.ResponseTimes.MEAN);
    Number stddev[]= (Number[])request.getAttribute(Constants.ResponseTimes.STDDEV);
    Number times[] = (Number[])request.getAttribute(Constants.TIME);
    
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
        Feel free to hide/show specific percentile by clicking on chart's legend
    </p>
    <ul class="nav nav-pills">
		<li class="nav-item"><a class="nav-link active">Responses</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/actions<%=path%>">Performed actions</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/gc<%=path%>">Garbage Collection</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/top<%=path%>">Top data</a></li>
	</ul>
</div>

<div class="container" id="response-chart-container" style="height:600px;">
</div>


<script>
var p50 = [];
var p95 = [];
var p99 = [];
var p999 = [];
var p100 = [];
var times = [];
var count = [];

<% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
    p50.push([new Date(<%= times[i] %>), <%= java.lang.Math.round(p50[i].doubleValue()) %>]);
    p95.push([new Date(<%= times[i] %>), <%= java.lang.Math.round(p95[i].doubleValue()) %>]);
    p99.push([new Date(<%= times[i] %>), <%= java.lang.Math.round(p99[i].doubleValue()) %>]);
    p999.push([new Date(<%= times[i] %>), <%= java.lang.Math.round(p999[i].doubleValue()) %>]);
	p100.push([new Date(<%= times[i] %>), <%= java.lang.Math.round(p100[i].doubleValue()) %>]);
    count.push(<%= java.lang.Math.round(count[i].doubleValue()) %>)
<% } %>


document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

if(localStorage.getItem('p50')==null){
    localStorage.setItem('p50', 'false');
}
if(localStorage.getItem('p95')==null){
    localStorage.setItem('p95', 'false');
}
if(localStorage.getItem('p99')==null){
    localStorage.setItem('p99', 'true');
}
if(localStorage.getItem('p999')==null){
    localStorage.setItem('p999', 'false');
}
if(localStorage.getItem('p100')==null){
    localStorage.setItem('p100', 'false');
}


var p50visible = localStorage.getItem('p50')==='true';
var p95visible = localStorage.getItem('p95')==='true';
var p99visible = localStorage.getItem('p99')==='true';
var p999visible = localStorage.getItem('p999')==='true';
var p100visible = localStorage.getItem('p100')==='true';

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

var myChart = Highcharts.chart('response-chart-container', {
        chart: {
                zoomType: 'x,y'
            },

        title: {
            text: 'Response times'
        },

        tooltip: {
            formatter: function() {
                            var index = this.point.index;
                            var date =  new Date(times[index]);
                            var samples = count[index];
                            return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                            + '<br/> <b>'+this.series.name+'</b> '+ this.y + ' ms <br/>'+
                            'Count:'+samples;
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
                text: 'Response time'
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
                            localStorage.setItem('p50', !series[0].visible);
                        }
                        if(event.target.index==1){
                            localStorage.setItem('p95', !series[1].visible);
                        }
                        if(event.target.index==2){
                            localStorage.setItem('p99', !series[2].visible);
                        }
                        if(event.target.index==3){
                            localStorage.setItem('p999', !series[3].visible);
                        }
                        if(event.target.index==4){
                            localStorage.setItem('p100', !series[4].visible);
                        }
                    }
                }
            }
        },
        series: [{
            name: '50%',
            data: p50,
            visible: p50visible,
            turboThreshold: 10000
        }, {
            name: '95%',
            data: p95,
            visible: p95visible,
            turboThreshold: 10000
        }, {
            name: '99%',
            data: p99,
            visible: p99visible,
            turboThreshold: 10000
        }, {
            name: '99.9%',
            data: p999,
            visible: p999visible,
            turboThreshold: 10000
        }, {
            name: 'max%',
            data: p100,
            visible: p100visible,
            turboThreshold: 10000
        }]
});
</script>

<div class="container scroll-container" >
    <table class="table table-fixed header-fixed">
        <thead class="thead-inverse">
            <th class="col-xs-3">Time</th>
            <th class="col-xs-1">Count</th>
            <th class="col-xs-1">Errors</th>
            <th class="col-xs-1">Mean</th>
            <th class="col-xs-1">Stddev</th>
            <th class="col-xs-1">50%</th>
            <th class="col-xs-1">95%</th>
            <th class="col-xs-1">99%</th>
            <th class="col-xs-1">99.9%</th>
            <th class="col-xs-1">Max</th>
        </thead>
        <tbody>
            <% for(int i=0;i<times.length;i++) {%>
                <tr class="row">
                    <td class="col-xs-3" style="text-align:center;">
                       <%= new java.util.Date(times[i].longValue()).toString() %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(count[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(errors[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(mean[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(stddev[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(p50[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(p95[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(p99[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(p999[i].doubleValue()) %>
                    </td>
                    <td class="col-xs-1">
                        <%= java.lang.Math.round(p100[i].doubleValue()) %>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>
</body>

</html>