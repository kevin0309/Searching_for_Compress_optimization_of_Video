<%@page import="framework.init.ServerConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Management</title>
<link rel="stylesheet" type="text/css" href="/EncodingServer/weblib/css/axicon/axicon.min.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<style type="text/css">
	.card {
		padding: 20px;
		display: inline-block;
		border: 1px solid #aaa;
	}
	
	.table.table-sm td, .table.table-sm th {
		vertical-align: middle;
	}
	
	.table.table-sm th {
		text-align: center;
	}
	
	.table.table-sm .td-center {
		text-align: center;
	}
</style>
<script type="text/javascript">
	$('document').ready(function() {
		
	});
	
	function makeList(defAmount, url, callback) {
		this.offset = 0;
		this.amount = defAmount;
		this.url = url;
		this.callback = callback;
	}
	
	makeList.prototype.refresh = function() {
		var _this = this;
		var param = {};
		param.offset = this.offset;
		param.amount = this.amount;
		$.post(this.url, param, this.callback);
	};
	
	makeList.prototype.nextPage = function() {
		if (this.offset + this.amount <= this.total)
			this.offset += this.amount;
		this.refresh();
	}
	
	makeList.prototype.prevPage = function() {
		if (this.offset - this.amount > -1)
			this.offset -= this.amount;
		this.refresh();
	}
	
	/*var serverList = new makeList(10, '/EncodingServer/process/getServerList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resServerList = resultJSON.resultData.serverList;
		$('#server-list tbody').empty();
		serverList.total = resEnt.total;
		serverList.list = resServerList;
		
		for (var i in resServerList) {
			var temp = resServerList[i];
			var $tr = $('<tr></tr>');
			$tr.append('<td class="td-center">' + temp.seq + '</td>');
			$tr.append('<td>' + temp.desc + '</td>');
			if (temp.status == '1')
				$tr.append('<td><a href="http://'+temp.addr+'/EncodingServer/" target="_blank">'+temp.addr+'</a></td>');
			else
				$tr.append('<td>' + temp.addr + '</td>');
			$tr.append('<td class="td-center">' + temp.macAddr + '</td>');
			if (temp.status == '1')
				$tr.append('<td class="td-center">online</td>');
			else
				$tr.append('<td class="td-center" style="color: #aaa;">offline</td>');
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			$('#server-list tbody').append($tr);
		}
		sampleVideoList.refresh(0, 20);
		encodingQueueList.refresh(0, 30);
	})*/
</script>
</head>
<body>
<div class="container">
<h1 style="margin: 50px auto;">Searching for Compress optimization of Video</h1>
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
			<a href="#">Encoding server management</a>
		</li>
		<li>
			<a href="#">Learning server management</a>
		</li>
	</ol>
</div>
</div>
</body>
</html>