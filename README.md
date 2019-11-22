# Searching_for_Compress_optimization_of_Video
단국대학교 2019년 2학기 실무중심산학협력프로젝트2(캡스톤디자인-SW) 과목에서 진행하는 팀프로젝트.<br>
프로젝트의 주제는 웹 스트리밍을 위한 영상 압축 손실 과정에서 최적의 설정값을 찾기위한 연구이다.<br>
<br>
코딩작업은 branch를 만들어서 작업하고<br>
절대 임의로 master branch를 수정하지 말것!!<br>
<br>
## 프로젝트 참여자
실무중심산학협력프로젝트2(캡스톤디자인-SW) 7분반 4조
- 단국대학교 3학년 32141868 박유현
- 단국대학교 3학년 32144697 최광진

## 프로젝트 구성
본 프로젝트는 크게 세가지의 어플리케이션과 데이터베이스서버로 구성되어있음.<br>
### 1. SCV
관리자 및 사용자에게 UI를 제공해주기 위한 웹 어플리케이션
### 개발/실행 환경
```
- OS : Microsoft Windows 10
- Language : JRE 1.8
- IDE : Eclipse Jee
- Web Server : Tomcat 8.0
```
### 2. EncodingServer
인코딩 연산을 진행하는 서버
<br>
http://welcome.blog-yh.kr/EncodingServer/
### 개발/실행 환경
```
- OS : Microsoft Windows 10
- Language : JRE 1.8
- IDE : Eclipse Jee
- Web Server : Tomcat 8.0
- 반드시 8080 포트에서 실행시킬 것
```
### 3. LearningServer
인코딩 연산 결과물을 분류 및 학습하는 서버
### 개발/실행 환경
```
- OS : 
- Language : Python
- IDE : 
- Web Server : 
```
### 4. 데이터베이스 서버
(소스코드 존재하지 않음.)
### 개발/실행 환경
```
- OS : Microsoft Windows 10
- DBMS : MySQL 8.0
- IP Address : 112.186.29.44
- Port : 3306
- DB name : scv
```

## 사용중인 외부 API
### Back-end
```
- FFMPEG (https://www.ffmpeg.org/)
- COS.jar
- JSON-simple-1.1.1.jar
- juniversechardet-2.1.0.jar (https://github.com/albfernandez/juniversalchardet)
- structural-similarity (https://github.com/rhys-e/structural-similarity)
```
### Front-end
```
- jQuery (https://jquery.com/)
- Bootstrap (https://getbootstrap.com/)
- toastr (https://github.com/CodeSeven/toastr)
- jQuery-contextMenu (https://github.com/swisnl/jQuery-contextMenu)
- jQuery-bgswitcher (https://github.com/rewish/jquery-bgswitcher)
- Video.js (https://videojs.com/)
- svideojs-quality-selector (https://github.com/silvermine/videojs-quality-selector)
- AXICON (https://axisj.com/axicon/)
- amCharts 4 (https://www.amcharts.com/)
```
