<%@page import="ru.naumen.perfhouse.statdata.DataType"%>
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
    Number add[]=  (Number[])request.getAttribute(Constants.PerformedActions.ADD_ACTIONS);
    Number edit[] = (Number[])request.getAttribute(Constants.PerformedActions.EDIT_ACTIONS);
    Number list[] = (Number[])request.getAttribute(Constants.PerformedActions.LIST_ACTIONS);
    Number comment[] = (Number[])request.getAttribute(Constants.PerformedActions.COMMENT_ACTIONS);
    Number form[] = (Number[])request.getAttribute(Constants.PerformedActions.GET_FORM_ACTIONS);
    Number dtos[] = (Number[])request.getAttribute(Constants.PerformedActions.GET_DT_OBJECT_ACTIONS);
    Number search[] = (Number[])request.getAttribute(Constants.PerformedActions.SEARCH_ACTIONS);
    Number getCatalog[] = (Number[])request.getAttribute(Constants.PerformedActions.GET_CATALOG_ACTIONS);
    Number actionsSumm[] = (Number[])request.getAttribute(Constants.PerformedActions.ACTIONS_COUNT);
    
    
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
		<li class="nav-item"><a class="nav-link active">Performed actions</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/gc<%=path%>">Garbage Collection</a></li>
		<li class="nav-item"><a class="btn btn-outline-primary" href="/history/${client}<%=custom %>/top<%=path%>">Top data</a></li>
	</ul>
</div>

<!-- Gc chart -->
<div class="container">
<div id="actions-chart-container" style="height: 600px"></div>
<div class="scroll-container">
	<table class="table table-fixed header-fixed">
        <thead class="thead-inverse">
            <th class="col-xs-3">Time</th>
            <th class="col-xs-1">Summ</th>
            <th class="col-xs-1">Addobject</th>
            <th class="col-xs-1">EditObject</th>
            <th class="col-xs-1">GetList</th>
            <th class="col-xs-1">Comment</th>
            <th class="col-xs-1">GetForm</th>
            <th class="col-xs-1">GetDtObject</th>
            <th class="col-xs-1">Search</th>
            <th class="col-xs-1">GetCatalog</th>
        </thead>
        <tbody >
            <% for(int i=0;i<times.length;i++) {%>
                <tr class="row">
                    <td class="col-xs-3" style="text-align:center;">
                       <%= new java.util.Date(times[i].longValue()).toString() %>
                    </td>
                    <td class="col-xs-1">
                        <%= actionsSumm[i].intValue() %>
                    </td>
                    <td class="col-xs-1" >
                        <%= add[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= edit[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= list[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= comment[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= form[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= dtos[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= search[i].intValue() %>
                    </td>
                    <td class="col-xs-1">
                        <%= getCatalog[i].intValue() %>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

</div>
<script type="text/javascript">
var times = [];
var add = [];
var edit = [];
var list = [];
var comment = [];
var form = [];
var dtos = [];
var search = [];
var catalog = [];
var summ = [];

<% for(int i=0;i<times.length;i++) {%>
    times.push((<%=times[i]%>));
    add.push([new Date(<%= times[i] %>), <%= add[i].intValue() %>]);
    edit.push([new Date(<%= times[i] %>), <%= edit[i].intValue() %>]);
    list.push([new Date(<%= times[i] %>), <%= list[i].intValue() %>]);
    comment.push([new Date(<%= times[i] %>), <%= comment[i].intValue() %>]);
    form.push([new Date(<%= times[i] %>), <%= form[i].intValue() %>]);
    dtos.push([new Date(<%= times[i] %>), <%= dtos[i].intValue() %>]);
    search.push([new Date(<%= times[i] %>), <%= search[i].intValue() %>]);
    catalog.push([new Date(<%= times[i] %>), <%= getCatalog[i].intValue() %>]);
    summ.push([new Date(<%= times[i] %>), <%= actionsSumm[i].intValue() %>]);

<% } %>

document.getElementById('date_range').innerHTML += 'From: '+new Date(times[<%=times.length%>-1])+'<br/>To:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +new Date(times[0])

if(localStorage.getItem('addActions')==null){
    localStorage.setItem('addActions', 'false');
}
if(localStorage.getItem('editActions')==null){
    localStorage.setItem('editActions', 'false');
}
if(localStorage.getItem('listActions')==null){
    localStorage.setItem('listActions', 'true');
}
if(localStorage.getItem('commentActions')==null){
    localStorage.setItem('commentActions', 'true');
}
if(localStorage.getItem('formActions')==null){
    localStorage.setItem('formActions', 'true');
}
if(localStorage.getItem('dtObjectActions')==null){
    localStorage.setItem('dtObjectActions', 'true');
}
if(localStorage.getItem('searchActions')==null){
    localStorage.setItem('searchActions', 'true');
}
if(localStorage.getItem('summary')==null){
    localStorage.setItem('summary', 'true');
}
if(localStorage.getItem('getCatalogActions')==null){
    localStorage.setItem('getCatalogActions', 'true');
}

var addVisible = localStorage.getItem('addActions')==='true';
var editVisible = localStorage.getItem('editActions')==='true';
var listVisible = localStorage.getItem('listActions')==='true';
var commentVisible = localStorage.getItem('commentActions')==='true';
var	formVisible = localStorage.getItem('formActions')==='true';
var dtosVisible = localStorage.getItem('dtObjectActions')==='true';
var searchVisible = localStorage.getItem('searchActions')==='true';
var catalogVisible = localStorage.getItem('getCatalogActions')==='true';
var summVisible = localStorage.getItem('summary')==='true';

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

var myChart = Highcharts.chart('actions-chart-container', {
        chart: {
                zoomType: 'x,y'
            },

        title: {
            text: 'Performed actions'
        },

        tooltip: {
            formatter: function() {
                            var index = this.point.index;
                            var date =  new Date(times[index]);
                            return Highcharts.dateFormat('%a %d %b %H:%M:%S', date)
                            + '<br/> <b>'+this.series.name+'</b> '+ this.y + ' times <br/>'
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
                text: 'Actions'
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
                            localStorage.setItem('addActions', !series[0].visible);
                        }
                        if(event.target.index==1){
                            localStorage.setItem('editActions', !series[1].visible);
                        }
                        if(event.target.index==2){
                            localStorage.setItem('listActions', !series[2].visible);
                        }
                        if(event.target.index==3){
                            localStorage.setItem('commentActions', !series[3].visible);
                        }
                        if(event.target.index==4){
                            localStorage.setItem('formActions', !series[4].visible);
                        }
                        if(event.target.index==5){
                            localStorage.setItem('dtObjectActions', !series[5].visible);
                        }
                        if(event.target.index==6){
                            localStorage.setItem('searchActions', !series[6].visible);
                        }
                        if(event.target.index==7){
                            localStorage.setItem('summary', !series[7].visible);
                        }
                        if(event.target.index==8){
                            localStorage.setItem('getCatalogActions', !series[8].visible);
                        }
                    }
                }
            }
        },
        series: [{
            name: 'AddObject',
            data: add,
            visible: addVisible,
            turboThreshold: 10000
        }, {
            name: 'EditObject',
            data: edit,
            visible: editVisible,
            turboThreshold: 10000
        }, {
            name: 'GetList',
            data: list,
            visible: listVisible,
            turboThreshold: 10000
        }
        , {
            name: 'Comment',
            data: comment,
            visible: commentVisible,
            turboThreshold: 10000
        }, {
            name: 'GetForm',
            data: form,
            visible: formVisible,
            turboThreshold: 10000
        }, {
            name: 'GetDtObject',
            data: dtos,
            visible: dtosVisible,
            turboThreshold: 10000
        }, {
            name: 'Search',
            data: search,
            visible: searchVisible,
            turboThreshold: 10000
        }, {
            name: 'Summary',
            data: summ,
            visible: summVisible,
            turboThreshold: 10000
        }, {
            name: 'GetCatalog',
            data: catalog,
            visible: catalogVisible,
            turboThreshold: 10000
        }]
});

</script>

</body>

</html>