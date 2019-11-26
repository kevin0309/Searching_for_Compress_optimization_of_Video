<%@page import="framework.init.ServerConfig"%>
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
<link rel="stylesheet" type="text/css" href="http://welcome.blog-yh.kr/EncodingServer/weblib/js/toast-master/toastr.min.css" />
<link rel="stylesheet" type="text/css" href="http://welcome.blog-yh.kr/EncodingServer/weblib/css/util.css" />
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<script src="https://www.amcharts.com/lib/4/core.js"></script>
<script src="https://www.amcharts.com/lib/4/charts.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/material.js"></script>
<script src="https://www.amcharts.com/lib/4/lang/de_DE.js"></script>
<script src="https://www.amcharts.com/lib/4/geodata/germanyLow.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>
<script type="text/javascript" src="http://welcome.blog-yh.kr/EncodingServer/weblib/js/toast-master/toastr.min.js"></script>
<script type="text/javascript" src="http://welcome.blog-yh.kr/EncodingServer/weblib/js/util.js"></script>
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
	
	.sample-video-area {
		display: inline-block;
		width: 49%;
		text-align: center;
		padding-top: 0.4rem;
	}
</style>
<script type="text/javascript">
	$('document').ready(function() {
		serverList.refresh();
		presetList.refresh();
		initChart();
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
		sampleVideoList.refresh();
		encodingQueueList.refresh();
	});
	
	var presetList = new makeList(10, '/SCV/process/getPresetList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resPresetList = resultJSON.resultData.presetList;
		$('#preset-list tbody').empty();
		presetList.total = resEnt.total;
		presetList.list = resPresetList;
		
		for (var i in resPresetList) {
			var temp = resPresetList[i];
			var $tr = $('<tr></tr>');
			$tr.append('<td>' + temp.code + '</td>');
			$tr.append('<td>' + temp.name + '</td>');
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			$('#server-list tbody').append($tr);
		}
	});
	
	var sampleVideoList = new makeList(10, '/SCV/process/getSampleVideoList', function(resultJSON) {
		var resEnt = resultJSON.resultData.result_entries;
		var resFileList = resultJSON.resultData.fileList;
		$('#sample-video-list tbody').empty();
		sampleVideoList.total = resEnt.total;
		sampleVideoList.list = resFileList;
		
		if (sampleVideoList.total == 0) {
			$('#sample-video-list tbody').append(
				'<tr>' +
					'<td colspan="12" class="td-center" style="color: #d44;">No result</td>' +
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
			$tr.append('<td class="td-center">' + temp.storedServerId + '</td>');
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			if (tempServer.status == 1)
				$tr.append('<td class="td-center"><a href="http://'+tempServer.addr+'/EncodingServer/download/original?id='+temp.seq+'">link</a></td>');
			else
				$tr.append('<td class="td-center" style="color: #aaa;">link</td>');
			$('#sample-video-list tbody').append($tr);
		}
	});
	
	var encodingQueueList = new makeList(20, '/SCV/process/getEncodingQueue', function(resultJSON) {
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
			$tr.append('<td class="td-center"><input type="checkbox" class="queue-check" onchange="chartDataGenerator.check(this)"></td>');
			$tr.append('<td class="td-center">' + temp.seq + '</td>');
			$tr.append('<td class="td-center">' + temp.fileId + '</td>');
			$tr.append('<td>' + temp.presetCode + '</td>');
			$tr.append('<td class="td-center">' + temp.status + '</td>');
			var vd;
			if (temp.volumeDifference <= 0)
				vd = $$.convertToVolumeStr(-temp.volumeDifference);
			else
				vd = '-' + $$.convertToVolumeStr(temp.volumeDifference);
			$tr.append('<td style="text-align: right;">' + vd + '</td>');
			$tr.append('<td class="td-center">' + temp.elapsedTime/1000 + '초</td>')
			$tr.append('<td class="td-center">' + temp.regdate + '</td>');
			$tr.append('<td>' + temp.ssim + '</td>');
			$tr.append('<td class="td-center">' + temp.assignedServerId + '</td>');
			if (chartDataGenerator.queueIndexList.has(temp.seq))
				$tr.find('td:eq(0) input').prop('checked', true);
			$('#encoded-video-list tbody').append($tr);
		}
	})
	
	function queueCheck(dom) {
		if ($(dom).prop('checked'))
			chartDataQueueIndexList.add(Number($(dom).parent().parent().find('td:eq(1)').text()));
		else
			chartDataQueueIndexList.delete(Number($(dom).parent().parent().find('td:eq(1)').text()));
		console.log(chartDataQueueIndexList);
	}
	
	var chartDataGenerator = {
		queueIndexList : new Set(),
		chartData : [],
		check : function(dom) {
			var tempIndex = Number($(dom).parent().parent().find('td:eq(1)').text());
			if ($(dom).prop('checked')) {
				this.queueIndexList.add(tempIndex);
				toastr["success"](tempIndex + ' - 결과차트에 추가');
			}
			else {
				this.queueIndexList.delete(tempIndex);
				toastr["warning"](tempIndex + ' - 결과차트에서 제외');
			}
			this.getEntries();
		},
		getEntries : function() {
			var _this = this;
			var param = {};
			param.data = this.queueIndexList.toJSON().toString();
			this.chartData = [];
			$.post('/SCV/process/getEncodingQueue', param, function (json) {
				var queueList = json.resultData.encodingQueueList;
				for (var i in queueList) {
					_this.chartData.push({
						preset : queueList[i].seq + '-' + queueList[i].presetCode,
						duration : queueList[i].elapsedTime/1000,
						volumeDiff : -queueList[i].volumeDifference,
						ssim : queueList[i].ssim
					});
				}
				chart.data = _this.chartData;
			});
		}
	}
	
	var chart;
	function initChart() {
		// Themes begin
		am4core.useTheme(am4themes_animated);
		// Themes end
		
		// Create chart instance
		chart = am4core.create("chartdiv", am4charts.XYChart);
		
		// Increase contrast by taking evey second color
		chart.colors.step = 2;
		chart.sequencedInterpolationDelay = 1000;
		// Create axes
		var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
		categoryAxis.dataFields.category = 'preset';
		categoryAxis.title.text = 'Encoding preset code';
		categoryAxis.renderer.minGridDistance = 50;
		
		// Create series
		//Volume Difference
		var volumeDiffAxis = chart.yAxes.push(new am4charts.ValueAxis());
		  
		var volumeDiffSeries = chart.series.push(new am4charts.LineSeries());
		volumeDiffSeries.dataFields.valueY = 'volumeDiff';
		volumeDiffSeries.dataFields.categoryX = "preset";
		volumeDiffSeries.strokeWidth = 2;
		volumeDiffSeries.yAxis = volumeDiffAxis;
		volumeDiffSeries.name = 'Volume Difference';
		volumeDiffSeries.tooltipText = "{name}: [bold]{valueY.formatNumber('0.00 b')}[/]";
		volumeDiffSeries.tensionX = 0.8;
		
		var interfaceColors = new am4core.InterfaceColorSet();

		var bullet = volumeDiffSeries.bullets.push(new am4charts.CircleBullet());
		bullet.circle.stroke = interfaceColors.getFor("background");
		bullet.circle.strokeWidth = 2;

		volumeDiffAxis.title.text = 'Volume Difference (byte)';
		volumeDiffAxis.renderer.line.strokeOpacity = 1;
		volumeDiffAxis.renderer.line.strokeWidth = 2;
		volumeDiffAxis.renderer.line.stroke = volumeDiffSeries.stroke;
		volumeDiffAxis.renderer.labels.template.fill = volumeDiffSeries.stroke;
		volumeDiffAxis.renderer.opposite = false;
		volumeDiffAxis.renderer.grid.template.disabled = true;
		volumeDiffAxis.renderer.inversed = true;
		
		//Duration
		var durationAxis = chart.yAxes.push(new am4charts.DurationAxis());
		
		var durationSeries = chart.series.push(new am4charts.LineSeries());
		durationSeries.dataFields.valueY = "duration";
		durationSeries.dataFields.categoryX = "preset";
		durationSeries.strokeWidth = 2;
		durationSeries.yAxis = durationAxis;
		durationSeries.name = "Duration";
		durationSeries.tooltipText = "{name}: [bold]{valueY} sec[/]";
		durationSeries.tensionX = 0.8;
		
		var interfaceColors = new am4core.InterfaceColorSet();
		
		var bullet = durationSeries.bullets.push(new am4charts.Bullet());
		bullet.width = 12;
		bullet.height = 12;
		bullet.horizontalCenter = "middle";
		bullet.verticalCenter = "middle";
		
		var triangle = bullet.createChild(am4core.Triangle);
		triangle.stroke = interfaceColors.getFor("background");
		triangle.strokeWidth = 2;
		triangle.direction = "top";
		triangle.width = 12;
		triangle.height = 12;

		durationAxis.renderer.line.strokeOpacity = 1;
		durationAxis.renderer.line.strokeWidth = 2;
		durationAxis.renderer.line.stroke = durationSeries.stroke;
		durationAxis.renderer.labels.template.fill = durationSeries.stroke;
		durationAxis.renderer.opposite = true;
		durationAxis.renderer.grid.template.disabled = true;
		durationAxis.renderer.inversed = true;

		//SSIM
		var ssimAxis = chart.yAxes.push(new am4charts.ValueAxis());
		
		var ssimSeries = chart.series.push(new am4charts.LineSeries());
		ssimSeries.dataFields.valueY = 'ssim';
		ssimSeries.dataFields.categoryX = "preset";
		ssimSeries.strokeWidth = 2;
		ssimSeries.yAxis = ssimAxis;
		ssimSeries.name = "SSIM";
		ssimSeries.tooltipText = "{name}: [bold]{valueY.formatNumber('#.########')}[/]";
		ssimSeries.tensionX = 0.8;
		
		var interfaceColors = new am4core.InterfaceColorSet();
		
		var bullet = ssimSeries.bullets.push(new am4charts.Bullet());
		bullet.width = 10;
		bullet.height = 10;
		bullet.horizontalCenter = "middle";
		bullet.verticalCenter = "middle";
		
		var rectangle = bullet.createChild(am4core.Rectangle);
		rectangle.stroke = interfaceColors.getFor("background");
		rectangle.strokeWidth = 2;
		rectangle.width = 10;
		rectangle.height = 10;
		
		ssimAxis.renderer.line.strokeOpacity = 1;
		ssimAxis.renderer.line.strokeWidth = 2;
		ssimAxis.renderer.line.stroke = ssimSeries.stroke;
		ssimAxis.renderer.labels.template.fill = ssimSeries.stroke;
		ssimAxis.renderer.opposite = true;
		ssimAxis.renderer.grid.template.disabled = true;


		// Add legend
		chart.legend = new am4charts.Legend();

		// Add cursor
		chart.cursor = new am4charts.XYCursor();
	}
	
	var videoController = {
			muted: false,
			play: function() {
				$('#sample-video-first video').get(0).play();
				$('#sample-video-second video').get(0).play();
			},
			pause: function() {
				$('#sample-video-first video').get(0).pause();
				$('#sample-video-second video').get(0).pause();
			},
			reload: function() {
				var input1 = $('#sample-video-first input').val();
				var input2 = $('#sample-video-second input').val();
				var param = {};
				param.data = input1+','+input2;
				var valFlag;
				if (input1 == '0' && input2 == '0')
					valFlag = 0;
				else if (input1 != '0' && input2 == '0')
					valFlag = 1;
				else if (input1 == '0' && input2 != '0')
					valFlag = 2;
				else
					valFlag = 3;
				$.post('/SCV/process/getEncodingQueue', param, function (json) {
					var queueList = json.resultData.encodingQueueList;
					var hostName1;
					var hostName2;
					if (valFlag == 0)
						return;
					else if (valFlag == 1) {
						for (var j in serverList.list)
							if (serverList.list[j].seq == queueList[0].assignedServerId)
								hostName1 = serverList.list[j].addr;
						$('#sample-video-first video').get(0).src = 'http://' + hostName1 + '/EncodingServer/download/stream?id=' + $('#sample-video-first input').val();
						$('#sample-video-first video').get(0).load();
					}
					else if (valFlag == 2) {
						for (var j in serverList.list)
							if (serverList.list[j].seq == queueList[0].assignedServerId)
								hostName2 = serverList.list[j].addr;
						$('#sample-video-second video').get(0).src = 'http://' + hostName2 + '/EncodingServer/download/encoded?id=' + $('#sample-video-second input').val();
						$('#sample-video-second video').get(0).load();
					}
					else {
						for (var j in serverList.list)
							if (serverList.list[j].seq == queueList[0].assignedServerId)
								hostName1 = serverList.list[j].addr;
						for (var j in serverList.list)
							if (serverList.list[j].seq == queueList[1].assignedServerId)
								hostName2 = serverList.list[j].addr;
						$('#sample-video-first video').get(0).src = 'http://' + hostName1 + '/EncodingServer/download/encoded?id=' + $('#sample-video-first input').val();
						$('#sample-video-first video').get(0).load();
						$('#sample-video-second video').get(0).src = 'http://' + hostName2 + '/EncodingServer/download/encoded?id=' + $('#sample-video-second input').val();
						$('#sample-video-second video').get(0).load();
					}
				});
			},
			mute: function() {
				if (this.muted) {
					$('#video-mute-btn i').attr('class', 'axi axi-mute');
					$('#sample-video-first video').get(0).muted = false;
					$('#sample-video-second video').get(0).muted = false;
					this.muted = false;
				}
				else {
					$('#video-mute-btn i').attr('class', 'axi axi-volume-mute');
					$('#sample-video-first video').get(0).muted = true;
					$('#sample-video-second video').get(0).muted = true;
					this.muted = true;
				}
			}
	}
</script>
</head>
<body style="overflow-x: scroll;">
<div class="container-fluid">
<h2 style="margin: 30px auto;">Searching for Compress optimization of Video - Management <span style="font-size: medium;">version : <%=ServerConfig.getProjectVersion() %></span></h2>
<nav>
	<div class="nav nav-tabs" role="tablist">
		<a id="server-list-table-tab" class="nav-item nav-link active" data-toggle="tab" href="#server-list-table" role="tab" aria-controls="server-list-table" aria-selected="true">인코딩 서버 목록</a>
		<a id="sample-video-list-table-tab" class="nav-item nav-link" data-toggle="tab" href="#sample-video-list-table" role="tab" aria-controls="sample-video-list-table" aria-selected="true">샘플 비디오 목록</a>
		<a id="encoding-preset-list-tab" class="nav-item nav-link" data-toggle="tab" href="#encoding-preset-list" role="tab" aria-controls="encoding-preset-list" aria-selected="true">인코딩 옵션 관리</a>
		<a id="queue-list-table-tab" class="nav-item nav-link" data-toggle="tab" href="#queue-list-table" role="tab" aria-controls="queue-list-table" aria-selected="true">인코딩 큐 목록</a>
		<a id="add-queue-tab" class="nav-item nav-link" data-toggle="tab" href="#add-queue" role="tab" aria-controls="add-queue" aria-selected="true">인코딩 큐 추가</a>
		<a id="result-chart-tab" class="nav-item nav-link" data-toggle="tab" href="#result-chart" role="tab" aria-controls="result-chart" aria-selected="false" onclick="setTimeout(function() {chart.reinit()}, 100)">결과 차트</a>
		<a id="play-result-video-tab" class="nav-item nav-link" data-toggle="tab" href="#play-result-video" role="tab" aria-controls="play-result-video" aria-selected="true" onclick="videoController.reload()">결과 비디오 재생</a>
	</div>
</nav>

<div class="tab-content">
	<div id="server-list-table" class="tab-pane fade show active" role="tabpanel" aria-labelledby="server-list-table-tab">
		<div style="margin: 30px auto;">
			<table id="server-list" class="table table-striped table-bordered table-hover table-sm">
				<thead class="thead-dark">
					<tr>
						<th>번호</th>
						<th>서버별칭</th>
						<th>주소</th>
						<th>MAC</th>
						<th>상태</th>
						<th>최근 접속일자</th>
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
	<div id="sample-video-list-table" class="tab-pane fade show" role="tabpanel" aria-labelledby="sample-video-list-table-tab">
		<div style="margin: 30px auto;">
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
						<th>너비</th>
						<th>높이</th>
						<th>저장서버</th>
						<th>등록일자</th>
						<th>다운</th>
					</tr>
				</thead>
				<tbody>
				
				</tbody>
				<tfoot>
					<tr>
						<td colspan="12">
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
	</div>
	<div id="encoding-preset-list" class="tab-pane fade" role="tabpanel" aria-labelledby="encoding-preset-list-tab">
		<div style="display: inline-block; min-height: 50vh; background: #f00; width: 400px;">
			<table id="preset-list" class="table table-striped table-bordered table-hover table-sm">
				<thead class="thead-dark">
					<tr>
						<th>코드</th>
						<th>이름</th>
						<th>작성일</th>
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
		<div style="display: inline-block; min-height: 50vh; background: #0f0; width: calc(100vw - 452px);"></div>
		<div style="min-height: 50vh; background: #00f;"></div>
	</div>
	<div id="queue-list-table" class="tab-pane fade show" role="tabpanel" aria-labelledby="queue-list-table-tab">
		<div style="margin: 30px auto;">
			<table id="encoded-video-list" class="table table-striped table-bordered table-hover table-sm">
				<thead class="thead-dark">
					<tr>
						<th><input type="checkbox" id="queue-check-all" disabled="disabled"></th>
						<th>번호</th>
						<th>파일 ID</th>
						<th>프리셋 코드</th>
						<th>현재상태</th>
						<th>용량변화</th>
						<th>소요시간</th>
						<th>등록일자</th>
						<th>SSIM</th>
						<th>등록서버</th>
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
	<div id="add-queue" class="tab-pane fade" role="tabpanel" aria-labelledby="add-queue-tab">
		add queue
	</div>
	<div id="result-chart" class="tab-pane fade" role="tabpanel" aria-labelledby="result-chart-tab">
		<div id="chartdiv" style="height: calc(100vh - 150px);"></div>
	</div>
	<div id="play-result-video" class="tab-pane fade" role="tabpanel" aria-labelledby="play-result-video">
		<div>
			<div class="sample-video-area" id="sample-video-first">
				<h5>Sample video 1 seq : <input type="number" value="0" style="width: 5rem;"></h5>
				<video preload="none" style="width: 100%;" controls="controls"></video>
			</div>
			<div class="sample-video-area" id="sample-video-second">
				<h5>Sample video 2 seq : <input type="number" value="0" style="width: 5rem;"></h5>
				<video preload="none" muted="muted" style="width: 100%;" controls="controls"></video>
			</div>
			<hr style="margin-top: 0.2rem;">
		</div>
		<div class="video-controller" style="text-align: center;">
			<div class="btn-group">
				<button class="btn btn-outline-dark btn-lg" id="video-play-btn" onclick="videoController.play()" title="재생"><i class="axi axi-play2"></i></button>
				<button class="btn btn-outline-dark btn-lg" id="video-pause-btn" onclick="videoController.pause()" title="일시정지"><i class="axi axi-pause3"></i></button>
				<button class="btn btn-outline-dark btn-lg" id="video-reload-btn" onclick="videoController.reload()" title="리로드"><i class="axi axi-reload"></i></button>
				<button class="btn btn-outline-dark btn-lg" id="video-mute-btn" onclick="videoController.mute()" title="음소거"><i class="axi axi-mute"></i></button>
			</div>
		</div>
	</div>
</div>
</div>
</body>
</html>