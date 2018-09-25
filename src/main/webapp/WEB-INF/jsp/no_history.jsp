<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
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
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid black;
        }
    </style>
</head>

<body>
<div class="container">
	<br>
    <h1>Oh, snap! They have stolen our data!</h1>
    <br/>
    <h3>Please, try again later. May be they will return it</h3>
    <br/>
    <img src="/imgs/no_data.png">
    <ul class="nav nav-pills">
		<li class="nav-item"><button type="button" class="btn btn-success btn-lg" onclick="history.back()">Go back</button>
    	<li class="nav-item"><h3><a class="btn btn-danger btn-lg" href="/">Client list</a></h3>
    </ul>
</div>

</body>

</html>