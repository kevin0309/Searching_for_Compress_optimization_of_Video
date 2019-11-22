<%@page import="framework.init.ServerConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Management</title>
<link rel="stylesheet" type="text/css" href="/SCV/weblib/css/axicon/axicon.min.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
<h1 style="margin: 30px auto;">Searching for Compress optimization of Video</h1>
<div>
	<h2>Current server info</h2>
	<ul>
		<li>ip address : <%=ServerConfig.getIpAddr() %>:<%=ServerConfig.getServerPortNum() %></li>
		<li>mac address : <%=ServerConfig.getMacAddr() %></li>
		<li>version : <%=ServerConfig.getProjectVersion() %></li>
	</ul>
</div>

<div>
	<h2>index</h2>
	<ol>
		<li>
			<a href="/SCV/page/encoding/index">Encoding server management</a>
		</li>
		<li>
			<a href="#">Learning server management</a>
		</li>
	</ol>
</div>
</div>
</body>
</html>