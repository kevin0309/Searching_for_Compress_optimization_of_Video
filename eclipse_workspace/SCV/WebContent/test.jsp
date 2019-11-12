<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>title</title>
<link rel="stylesheet" type="text/css" href="/SCV/weblib/css/axicon/axicon.min.css">
<script type="text/javascript" src="//code.jquery.com/jquery-latest.min.js"></script>
<style type="text/css">

</style>
<script type="text/javascript">

</script>
</head>
<body onload="console.log(event);console.log(this);">
<h3>이벤트 객체의 프로퍼티 출력</h3>
<hr>
<p id="p">버튼을 클릭하면 이벤트 객체를 출력합니다.</p>
<button onclick="f(event)">클릭하세요</button>
<script>
function f(e) { // e는 현재 발생한 이벤트 객체
	var text = "type: " + e.type + "<br>" 
					+ "target: " + e.target + "<br>"
					+ "currentTarget: " + e.currentTarget + "<br>"
					+ "defaultPrevented: " + e.defaultPrevented;

	var p = document.getElementById("p");
	p.innerHTML = text; // 이벤트 객체의 프로퍼티 출력
}
</script>
</body>
</html>