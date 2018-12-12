<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.Set" %>
<%@page import="java.util.Date" %>
<%@page import="org.influxdb.dto.QueryResult.Series" %>
<%@page import="ru.naumen.sd40.log.parser.utils.GlobalConstants" %>
<%@page import="ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataType" %>
<%@page import="ru.naumen.sd40.log.parser.modes.common.DataType" %>

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
            if(day != null){
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
        <% for(String dataType: ((Set<String>)request.getAttribute("dataTypes"))) { %>
		  <li class="nav-item">
            <a class="btn btn-outline-primary" href="/history/${client}<%=custom%>/<%=dataType%><%=path%>">
                <%=dataType%>
            </a>
        </li>
        <% } %>
	</ul>
</div>
</body>

</html>