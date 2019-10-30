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
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<style type="text/css">
	.file-handler>div {
		width: calc(25% - 50px);
		padding: 20px;
		display: inline-block;
		border: 1px solid #aaa;
	}
</style>
<script type="text/javascript">
	$('document').ready(function() {
		serverList.refresh(0, 10);
		sampleVideoList.refresh(0, 20);
		encodingQueueList.refresh(0, 30);
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
			$tr.append('<td>' + temp.seq + '</td>');
			$tr.append('<td>' + temp.desc + '</td>');
			$tr.append('<td>' + temp.addr + '</td>');
			$tr.append('<td>' + temp.macAddr + '</td>');
			$tr.append('<td>' + temp.status + '</td>');
			$tr.append('<td>' + temp.regdate + '</td>');
			$('#server-list tbody').append($tr);
		}
	})
	
	var sampleVideoList = new makeList(10, '/EncodingServer/process/getSampleVideoList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resFileList = resultJSON.resultData.fileList;
		$('#sample-video-list tbody').empty();
		sampleVideoList.total = resEnt.total;
		sampleVideoList.list = resFileList;
		
		for (var i in resFileList) {
			var temp = resFileList[i];
			var tempServer;
			for (var j in serverList.list)
				if (serverList.list[j].seq === temp.storedServerId) {
					tempServer = serverList.list[j];
					break;
				}
			var $tr = $('<tr></tr>');
			$tr.append('<td>' + temp.seq + '</td>');
			$tr.append('<td>' + temp.fileName + '</td>');
			$tr.append('<td>' + temp.fileExt + '</td>');
			$tr.append('<td>' + temp.mimeType + '</td>');
			$tr.append('<td>' + temp.fileVolume + '</td>');
			$tr.append('<td>' + temp.vCodec + '</td>');
			$tr.append('<td>' + temp.aCodec + '</td>');
			$tr.append('<td>' + temp.width + '</td>');
			$tr.append('<td>' + temp.height + '</td>');
			$tr.append('<td>' + temp.storedServerId + '</td>');
			$tr.append('<td>' + temp.regdate + '</td>');
			if (tempServer.status == 1)
				$tr.append('<td><a href="http://'+tempServer.addr+'/EncodingServer/download/original?id='+temp.seq+'">go</a></td>');
			else
				$tr.append('<td>go</td>');
			$('#sample-video-list tbody').append($tr);
		}
	});
	
	var encodingQueueList = new makeList(20, '/EncodingServer/process/getEncodingQueueList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resEncodingQueueList = resultJSON.resultData.encodingQueueList;
		$('#encoded-video-list tbody').empty();
		encodingQueueList.total = resEnt.total;
		encodingQueueList.list = resEncodingQueueList;
		
		for (var i in resEncodingQueueList) {
			var temp = resEncodingQueueList[i];
			var tempServer;
			for (var j in serverList.list)
				if (serverList.list[j].seq === temp.assignedServerId) {
					tempServer = serverList.list[j];
					break;
				}
			var $tr = $('<tr></tr>');
			$tr.append('<td>' + temp.seq + '</td>');
			$tr.append('<td>' + temp.fileId + '</td>');
			$tr.append('<td>' + temp.presetCode + '</td>');
			$tr.append('<td>' + temp.status + '</td>');
			$tr.append('<td>' + temp.endVolume + '</td>');
			$tr.append('<td>' + temp.startDate + '</td>');
			$tr.append('<td>' + temp.endDate + '</td>');
			$tr.append('<td>' + temp.assignedServerId + '</td>');
			$tr.append('<td>' + temp.regdate + '</td>');
			if (tempServer.status == 1)
				$tr.append('<td><a href="http://'+tempServer.addr+'/EncodingServer/download/encoded?id='+temp.seq+'">go</a></td>');
			else
				$tr.append('<td>go</td>');
			if (tempServer.status == 1)
				$tr.append('<td><a href="http://'+tempServer.addr+'/EncodingServer/download/log?id='+temp.seq+'">go</a></td>');
			else
				$tr.append('<td>go</td>');
			$('#encoded-video-list tbody').append($tr);
		}
	});
</script>
</head>
<body>
<div class="container">
<h1>Encoding Server version <%=ServerConfig.getProjectVersion() %></h1>
<div class="file-handler">
	<div>
		<h2>Upload</h2>
		<form action="/EncodingServer/upload/file" method="post" enctype="multipart/form-data">
			<input type="file" name="upload_file">
			<input type="submit" value="gogo">
		</form>
	</div>
</div>

<div>
	<h2>서버 목록</h2>
	<table id="server-list" border="1">
		<thead>
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
						<button onclick="serverList.prevPage()">이전</button>
						<button onclick="serverList.refresh()">새로고침</button>
						<button onclick="serverList.nextPage()">다음</button>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</div>

<div>
	<h2>샘플 영상 목록</h2>
	<table id="sample-video-list" border="1">
		<thead>
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
				<th>저장된 서버 ID</th>
				<th>등록일자</th>
				<th>영상다운</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
		<tfoot>
			<tr>
				<td colspan="12">
					<div style="text-align: center;">
						<button onclick="sampleVideoList.prevPage()">이전</button>
						<button onclick="sampleVideoList.refresh()">새로고침</button>
						<button onclick="sampleVideoList.nextPage()">다음</button>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</div>

<div>
	<h2>인코딩 대기열 목록</h2>
	<table id="encoded-video-list" border="1">
		<thead>
			<tr>
				<th>번호</th>
				<th>파일 ID</th>
				<th>프리셋 코드</th>
				<th>현재상태</th>
				<th>작업 후 용량</th>
				<th>인코딩 시작 날짜</th>
				<th>인코딩 종료 날짜</th>
				<th>작업 할당된 서버 ID</th>
				<th>등록일자</th>
				<th>영상다운</th>
				<th>로그다운</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11">
					<div style="text-align: center;">
						<button onclick="encodingQueueList.prevPage()">이전</button>
						<button onclick="encodingQueueList.refresh()">새로고침</button>
						<button onclick="encodingQueueList.nextPage()">다음</button>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
</div>
</div>
</body>
</html>