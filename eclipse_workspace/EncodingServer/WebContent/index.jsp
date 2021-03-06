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
<script type="text/javascript" src="/EncodingServer/weblib/js/util.js"></script>
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
		serverList.refresh(0, 10);
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
	
	var serverList = new makeList(10, '/EncodingServer/process/getServerList', function(resultJSON) {
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
	
	var sampleVideoList = new makeList(10, '/EncodingServer/process/getSampleVideoList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resFileList = resultJSON.resultData.fileList;
		$('#sample-video-list tbody').empty();
		sampleVideoList.total = resEnt.total;
		sampleVideoList.list = resFileList;
		
		if (sampleVideoList.total == 0) {
			$('#sample-video-list tbody').append(
				'<tr>' +
					'<td colspan="11" class="td-center" style="color: #d44;">No result</td>' +
				'</tr>'
			);
			return;
		}
		
		for (var i in resFileList) {
			var temp = resFileList[i];
			var tempServer;
			for (var j in serverList.list)
				if (serverList.list[j].seq === temp.storedServerId) {
					tempServer = serverList.list[j];
					break;
				}
			var $tr = $('<tr></tr>');
			$tr.append('<td class="td-center">' + temp.seq + '</td>');
			$tr.append('<td>' + temp.fileName + '</td>');
			$tr.append('<td class="td-center">' + temp.fileExt + '</td>');
			$tr.append('<td>' + temp.mimeType + '</td>');
			$tr.append('<td class="td-center">' + temp.fileVolume + '</td>');
			$tr.append('<td class="td-center">' + temp.vCodec + '</td>');
			$tr.append('<td class="td-center">' + temp.aCodec + '</td>');
			$tr.append('<td class="td-center">' + temp.width + '</td>');
			$tr.append('<td class="td-center">' + temp.height + '</td>');
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			if (tempServer.status == 1)
				$tr.append('<td class="td-center"><a href="http://'+tempServer.addr+'/EncodingServer/download/original?id='+temp.seq+'">link</a></td>');
			else
				$tr.append('<td class="td-center" style="color: #aaa;">link</td>');
			$('#sample-video-list tbody').append($tr);
		}
	});
	
	var encodingQueueList = new makeList(20, '/EncodingServer/process/getEncodingQueueList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resEncodingQueueList = resultJSON.resultData.encodingQueueList;
		$('#encoded-video-list tbody').empty();
		encodingQueueList.total = resEnt.total;
		encodingQueueList.list = resEncodingQueueList;
		
		if (encodingQueueList.total == 0) {
			$('#encoded-video-list tbody').append(
				'<tr>' +
					'<td colspan="10" class="td-center" style="color: #d44;">No result</td>' +
				'</tr>'
			);
			return;
		}
		
		for (var i in resEncodingQueueList) {
			var temp = resEncodingQueueList[i];
			var tempServer;
			for (var j in serverList.list)
				if (serverList.list[j].seq === temp.assignedServerId) {
					tempServer = serverList.list[j];
					break;
				}
			var $tr = $('<tr></tr>');
			$tr.append('<td class="td-center">' + temp.seq + '</td>');
			$tr.append('<td class="td-center">' + temp.fileId + '</td>');
			$tr.append('<td class="td-center">' + temp.presetCode + '</td>');
			$tr.append('<td class="td-center">' + temp.status + '</td>');
			var vd;
			if (temp.volumeDifference <= 0)
				vd = $$.convertToVolumeStr(-temp.volumeDifference);
			else
				vd = '-' + $$.convertToVolumeStr(temp.volumeDifference);
			$tr.append('<td class="td-center">' + vd + '</td>');
			$tr.append('<td class="td-center">' + temp.elapsedTime/1000 + '초</td>')
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			$tr.append('<td class="td-center">' + temp.ssim + '</td>');
			if (tempServer.status == 1 && temp.status == 'finished')
				$tr.append('<td class="td-center"><a href="http://'+tempServer.addr+'/EncodingServer/download/encoded?id='+temp.seq+'">link</a></td>');
			else
				$tr.append('<td class="td-center">link</td>');
			if (tempServer.status == 1 && temp.status == 'finished')
				$tr.append('<td class="td-center"><a href="http://'+tempServer.addr+'/EncodingServer/download/log?id='+temp.seq+'">link</a></td>');
			else
				$tr.append('<td class="td-center">link</td>');
			$('#encoded-video-list tbody').append($tr);
		}
	});
</script>
</head>
<body>
<div class="container">
<h1 style="margin: 50px auto;">Encoding Server</h1>
<div>
	<h2>Current server info</h2>
	<ul>
		<li>id : <%=ServerConfig.getServerId() %></li>
		<li>name : <%=ServerConfig.getServerNickname() %></li>
		<li>ip address : <%=ServerConfig.getIpAddr() %>:<%=ServerConfig.getServerPortNum() %></li>
		<li>mac address : <%=ServerConfig.getMacAddr() %></li>
		<li>version : <%=ServerConfig.getProjectVersion() %></li>
	</ul>
</div>

<div style="margin: 30px auto;">
	<h2>Server list</h2>
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

<div class="file-handler" style="margin: 30px auto;">
	<div>
		<h2>Sample video upload</h2>
		<form action="/EncodingServer/upload/file" method="post" enctype="multipart/form-data">
			<input type="file" name="upload_file">
			<input type="submit" value="upload" class="btn btn-outline-success btn-sm">
		</form>
	</div>
</div>

<div style="margin: 30px auto;">
	<h2>Sample video list</h2>
	<table id="sample-video-list" class="table table-striped table-bordered table-hover table-sm">
		<thead class="thead-dark">
			<tr>
				<th>번호</th>
				<th>파일명</th>
				<th>확장자</th>
				<th>MIME</th>
				<th>용량</th>
				<th>비디오 코덱</th>
				<th>오디오 코덱</th>
				<th>비디오 너비</th>
				<th>비디오 높이</th>
				<th>등록일자</th>
				<th>영상다운</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11">
					<div style="text-align: center;">
						<button onclick="sampleVideoList.prevPage()" class="btn btn-outline-dark btn-sm">prev</button>
						<button onclick="sampleVideoList.refresh()" class="btn btn-outline-dark btn-sm">refresh</button>
						<button onclick="sampleVideoList.nextPage()" class="btn btn-outline-dark btn-sm">next</button>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</div>

<div style="margin: 30px auto;">
	<h2>Encoding queue list</h2>
	<table id="encoded-video-list" class="table table-striped table-bordered table-hover table-sm">
		<thead class="thead-dark">
			<tr>
				<th>번호</th>
				<th>파일 ID</th>
				<th>프리셋 코드</th>
				<th>현재상태</th>
				<th>용량변화</th>
				<th>소요시간</th>
				<th>등록일자</th>
				<th>SSIM</th>
				<th>영상다운</th>
				<th>로그다운</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
		<tfoot>
			<tr>
				<td colspan="10">
					<div style="text-align: center;">
						<button onclick="encodingQueueList.prevPage()" class="btn btn-outline-dark btn-sm">prev</button>
						<button onclick="encodingQueueList.refresh()" class="btn btn-outline-dark btn-sm">refresh</button>
						<button onclick="encodingQueueList.nextPage()" class="btn btn-outline-dark btn-sm">next</button>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</div>
</div>
</body>
</html>