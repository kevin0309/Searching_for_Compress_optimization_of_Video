<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Server list</title>
<link rel="stylesheet" type="text/css" href="/EncodingServer/weblib/css/axicon/axicon.min.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<script src="https://www.amcharts.com/lib/4/core.js"></script>
<script src="https://www.amcharts.com/lib/4/charts.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/material.js"></script>
<script src="https://www.amcharts.com/lib/4/lang/de_DE.js"></script>
<script src="https://www.amcharts.com/lib/4/geodata/germanyLow.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>
<style type="text/css">

</style>
<script type="text/javascript">
	$('document').ready(function() {
		test2();
	});
	
	function test() {
		var chart = am4core.create("chartdiv", "PieChart");
		var series = chart.series.push(new am4charts.PieSeries());
		series.dataFields.value = "litres";
		series.dataFields.category = "country";

		// Add data
		chart.data = [{
		  "country": "Lithuania",
		  "litres": 501.9
		}, {
		  "country": "Czech Republic",
		  "litres": 301.9
		}, {
		  "country": "Ireland",
		  "litres": 201.1
		}, {
		  "country": "Germany",
		  "litres": 165.8
		}, {
		  "country": "Australia",
		  "litres": 139.9
		}, {
		  "country": "Austria",
		  "litres": 128.3
		}, {
		  "country": "UK",
		  "litres": 99
		}, {
		  "country": "Belgium",
		  "litres": 60
		}, {
		  "country": "The Netherlands",
		  "litres": 50
		}];

		// And, for a good measure, let's add a legend
		chart.legend = new am4charts.Legend();
	}
	
	function test2() {
		// Themes begin
		am4core.useTheme(am4themes_animated);
		// Themes end

		// Create chart instance
		var chart = am4core.create("chartdiv", am4charts.XYChart);

		chart.colors.step = 2;
		chart.maskBullets = true;

		// Add data
		chart.data = [{
		    "date": "2012-01-01",
		    "distance": 227,
		    "townName": "New York",
		    "townName2": "New York",
		    "townSize": 12,
		    "latitude": 40.71,
		    "duration": 408
		}, {
		    "date": "2012-01-02",
		    "distance": 371,
		    "townName": "Washington",
		    "townSize": 7,
		    "latitude": 38.89,
		    "duration": 482
		}, {
		    "date": "2012-01-03",
		    "distance": 433,
		    "townName": "Wilmington",
		    "townSize": 3,
		    "latitude": 34.22,
		    "duration": 562
		}, {
		    "date": "2012-01-04",
		    "distance": 345,
		    "townName": "Jacksonville",
		    "townSize": 3.5,
		    "latitude": 30.35,
		    "duration": 379
		}, {
		    "date": "2012-01-05",
		    "distance": 480,
		    "townName": "Miami",
		    "townName2": "Miami",
		    "townSize": 5,
		    "latitude": 25.83,
		    "duration": 501
		}, {
		    "date": "2012-01-06",
		    "distance": 386,
		    "townName": "Tallahassee",
		    "townSize": 3.5,
		    "latitude": 30.46,
		    "duration": 443
		}, {
		    "date": "2012-01-07",
		    "distance": 348,
		    "townName": "New Orleans",
		    "townSize": 5,
		    "latitude": 29.94,
		    "duration": 405
		}, {
		    "date": "2012-01-08",
		    "distance": 238,
		    "townName": "Houston",
		    "townName2": "Houston",
		    "townSize": 8,
		    "latitude": 29.76,
		    "duration": 309
		}, {
		    "date": "2012-01-09",
		    "distance": 218,
		    "townName": "Dalas",
		    "townSize": 8,
		    "latitude": 32.8,
		    "duration": 287
		}, {
		    "date": "2012-01-10",
		    "distance": 349,
		    "townName": "Oklahoma City",
		    "townSize": 5,
		    "latitude": 35.49,
		    "duration": 485
		}, {
		    "date": "2012-01-11",
		    "distance": 603,
		    "townName": "Kansas City",
		    "townSize": 5,
		    "latitude": 39.1,
		    "duration": 890
		}, {
		    "date": "2012-01-12",
		    "distance": 534,
		    "townName": "Denver",
		    "townName2": "Denver",
		    "townSize": 9,
		    "latitude": 39.74,
		    "duration": 810
		}, {
		    "date": "2012-01-13",
		    "townName": "Salt Lake City",
		    "townSize": 6,
		    "distance": 425,
		    "duration": 670,
		    "latitude": 40.75,
		    "dashLength": 8,
		    "alpha": 0.4
		}, {
		    "date": "2012-01-14",
		    "latitude": 36.1,
		    "duration": 470,
		    "townName": "Las Vegas",
		    "townName2": "Las Vegas"
		}, {
		    "date": "2012-01-15"
		}, {
		    "date": "2012-01-16"
		}, {
		    "date": "2012-01-17"
		}];
		
		/* chart.data = [
			{
				"presetCode" : "crf_18_ultrafast",
				"duration" : 123,
				"volumeDiff" : 1024*1024*10,
				"ssim" : 0.951234
			},
			{
				"presetCode" : "crf_18_veryslow",
				"duration" : 1234,
				"volumeDiff" : -1024*1024*10,
				"ssim" : 0.971234
			}
		]; */

		// Create axes
		var presetCodeAxis = chart.xAxes.push(new am4charts.CategoryAxis());
		presetCodeAxis.renderer.grid.template.location = 0;
		presetCodeAxis.renderer.minGridDistance = 50;
		presetCodeAxis.renderer.grid.template.disabled = true;
		presetCodeAxis.renderer.fullWidthTooltip = true;

		var distanceAxis = chart.yAxes.push(new am4charts.ValueAxis());
		distanceAxis.title.text = "Volume difference";
		distanceAxis.renderer.grid.template.disabled = true;
		distanceAxis.numberFormatter.numberFormat  = "0.00 b";
		//distanceAxis.renderer.fullWidthTooltip = true;

		var durationAxis = chart.yAxes.push(new am4charts.DurationAxis());
		durationAxis.title.text = "Duration";
		durationAxis.baseUnit = "second";
		durationAxis.renderer.grid.template.disabled = true;
		durationAxis.renderer.opposite = true;

		durationAxis.durationFormatter.durationFormat = "hh:mm:ss";

		var latitudeAxis = chart.yAxes.push(new am4charts.ValueAxis());
		latitudeAxis.renderer.grid.template.disabled = true;
		latitudeAxis.renderer.labels.template.disabled = true;

		// Create series
		var distanceSeries = chart.series.push(new am4charts.ColumnSeries());
		distanceSeries.dataFields.valueY = "distance";
		distanceSeries.dataFields.dateX = "date";
		distanceSeries.yAxis = distanceAxis;
		distanceSeries.tooltipText = "{valueY} miles";
		distanceSeries.name = "Distance";
		distanceSeries.columns.template.fillOpacity = 0.7;
		distanceSeries.columns.template.propertyFields.strokeDasharray = "dashLength";
		distanceSeries.columns.template.propertyFields.fillOpacity = "alpha";

		var disatnceState = distanceSeries.columns.template.states.create("hover");
		disatnceState.properties.fillOpacity = 0.9;

		var durationSeries = chart.series.push(new am4charts.LineSeries());
		durationSeries.dataFields.valueY = "duration";
		durationSeries.dataFields.dateX = "date";
		durationSeries.yAxis = durationAxis;
		durationSeries.name = "Duration";
		durationSeries.strokeWidth = 2;
		durationSeries.propertyFields.strokeDasharray = "dashLength";
		durationSeries.tooltipText = "{valueY.formatDuration()}";

		var durationBullet = durationSeries.bullets.push(new am4charts.Bullet());
		var durationRectangle = durationBullet.createChild(am4core.Rectangle);
		durationBullet.horizontalCenter = "middle";
		durationBullet.verticalCenter = "middle";
		durationBullet.width = 7;
		durationBullet.height = 7;
		durationRectangle.width = 7;
		durationRectangle.height = 7;

		var durationState = durationBullet.states.create("hover");
		durationState.properties.scale = 1.2;

		var latitudeSeries = chart.series.push(new am4charts.LineSeries());
		latitudeSeries.dataFields.valueY = "latitude";
		latitudeSeries.dataFields.dateX = "date";
		latitudeSeries.yAxis = latitudeAxis;
		latitudeSeries.name = "Duration";
		latitudeSeries.strokeWidth = 2;
		latitudeSeries.propertyFields.strokeDasharray = "dashLength";
		latitudeSeries.tooltipText = "Latitude: {valueY} ({townName})";

		var latitudeBullet = latitudeSeries.bullets.push(new am4charts.CircleBullet());
		latitudeBullet.circle.fill = am4core.color("#fff");
		latitudeBullet.circle.strokeWidth = 2;
		latitudeBullet.circle.propertyFields.radius = "townSize";

		var latitudeState = latitudeBullet.states.create("hover");
		latitudeState.properties.scale = 1.2;

		var latitudeLabel = latitudeSeries.bullets.push(new am4charts.LabelBullet());
		latitudeLabel.label.text = "{townName2}";
		latitudeLabel.label.horizontalCenter = "left";
		latitudeLabel.label.dx = 14;

		// Add legend
		chart.legend = new am4charts.Legend();

		// Add cursor
		chart.cursor = new am4charts.XYCursor();
		chart.cursor.fullWidthLineX = true;
		chart.cursor.xAxis = presetCodeAxis;
		chart.cursor.lineX.strokeOpacity = 0.3;
		chart.cursor.lineX.fill = am4core.color("#000");
		chart.cursor.lineX.fillOpacity = 0.1;
	}
</script>
</head>
<body>
<div class="container">
<div>
	<h2>Finished encoding queue list</h2>
	<div id="chartdiv" style="width: 900px; height: 800px;"></div>
</div>
</div>
</body>
</html>