<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Server list</title>
<link rel="stylesheet" type="text/css" href="/SCV/weblib/css/axicon/axicon.min.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<style type="text/css">
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
		serverList.refresh();
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
	
	var serverList = new makeList(10, '/SCV/process/getServerList', function(resultJSON) {
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
	})
</script>
</head>
<body>
<div class="container">
	<h2>Server list</h2>
	
	<div style="margin: 30px auto;">
		<table id="server-list" class="table table-striped table-bordered table-hover table-sm">
			<thead class="thead-dark">
				<tr>
					<th>번호</th>
					<th>서버별칭</th>
					<th>주소</th>
					<th>MAC</th>
					<th>상태</th>
					<th>등록일자</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6">
						<div style="text-align: center;">
							<button onclick="serverList.prevPage()" class="btn btn-outline-dark btn-sm">prev</button>
							<button onclick="serverList.refresh()" class="btn btn-outline-dark btn-sm">refresh</button>
							<button onclick="serverList.nextPage()" class="btn btn-outline-dark btn-sm">next</button>
						</div>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
</body>
</html>