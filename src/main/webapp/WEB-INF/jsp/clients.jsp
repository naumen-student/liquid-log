<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<html>

<head>
    <title>SD40 Performance indicator</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<script src="/js/jquery-3.1.1.min.js"></script>
	
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css"
          integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous"/>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js"
            integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK"
            crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.2.0/js/tether.min.js"
        integrity="sha384-Plbmg8JY28KFelvJVai01l8WyZzrYWG825m+cZ0eDDS1f7d/js6ikvy1+X+guPIB"
        crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.17.1/moment.min.js"></script>
    <link rel="stylesheet" href="/css/style.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.css"/>
    
</head>

<body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript">
    	function setupModal(client){
    		$('#formFrom').val(moment().format("DD/MM/YYYY"))
    		$('#formTo').val(moment().format("DD/MM/YYYY"))
    		$('#formTo').datepicker({
    			format: "dd/mm/yyyy"
    		})
    		$('#formFrom').datepicker({
    			format: "dd/mm/yyyy",
    		})
    		
    		$('#formMaxResults').val(100)
    		$('#customForm').attr('action','/data_types/'+client+)
    		console.log(moment().format('zz'))
    	}
    </script>
<div class="container">
	<br>
	<div class="alert alert-info">
  		<h3><strong>Attention!</strong><br>All requests for stored data are made with UTC time.<br>Requested data will be displayed in your browsers timezone.</h3>
	</div>
	<br>
	<h1>Send log for parsing!</h1>

    <form action="/parse" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td>Database name:</td>
                <td><input type="text" class="form-control" name="db" required></td>
            </tr>
            <tr>
                <td>Log file path:</td>
                <td><input type="text" class="form-control" name="filepath" required></td>
            </tr>
            <tr>
                <td>Timezone:</td>
                <td><input type="text" class="form-control" name="timezone" placeholder="GMT"></td>
            </tr>
        </table>

        <label>Parsing Mode:
            <select name="parse_mode">
               <% for(String builder:(Set<String>)request.getAttribute("parseBuilders")) { %>
                      <option><%=builder%></option>
               <% } %>
            </select>
        </label>
        <br>
        <label>
            Need Logging:
            <select name="need_logging">
                <option value="yes" selected>yes</option>
                <option value="no">no</option>
            </select>
        </label>

        <br>
        <button type="submit" class="btn btn-success">Parse!</button>
    </form>

    <h1>Client list</h1>
    <table class="table table-striped table-fixed"> <!-- table-bordered  -->
        <thead class="thead-inverse">
            <th class="col-xs-6">Name</th>
            <th class="col-xs-6">Link</th>
        </thead>
         <tbody>
         <% for(String client:(List<String>)request.getAttribute("clients")) { %>
            <tr>
                <td class="col-xs-6">
                    <h4><span><%= client %></span></h2>
                </td>
                <td class="col-xs-6">
                	<a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("prevMonthLinks")).get(client) %>'>Previous Month</a>
                    <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("monthlinks")).get(client) %>'>Month</a>
                    <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("last2016links")).get(client) %>'>Last 7 days</a>
                    <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("last864links")).get(client) %>'>Last 3 days</a>
                    <a class="btn btn-outline-primary" href='<%= ((Map)request.getAttribute("links")).get(client) %>'>Yesterday</a>
                    <button type="button" class="btn btn-success" data-toggle="modal" data-target="#customModal" onclick="setupModal('<%=client %>')">Custom request</button>
                </td>
            </tr>
           <% } %>
        </tbody>
        </table>
</div>

<div class="modal fade" id="customModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class=modal-dialog role="dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Select dates and max results</h4>
			</div>
			<form id="customForm">
				<div class="modal-body">
				    <div class="row">
				        <div class="col-md-6">
                            <div class="form-group">
                                <label for="formFrom">From</label>
                                <input type="text"class="form-control" id="formFrom" name="from">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="formTo">To</label>
                                <input type="text" class="form-control" id="formTo" name="to">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="formCount">Max results</label>
                        <input class="form-control" type="number" value="42" id="formMaxResults" name="maxResults">
                    </div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-success">Request</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>
</body>

</html>