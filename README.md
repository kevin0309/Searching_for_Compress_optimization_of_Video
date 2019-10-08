# Searching_for_Compress_optimization_of_Video
단국대학교 2019년 2학기 실무중심산학협력프로젝트2(캡스톤디자인-SW) 과목에서 진행하는 팀프로젝트임.<br>
프로젝트의 주제는 웹 스트리밍을 위한 영상 압축 손실 과정에서 최적의 설정값을 찾기위한 연구이다.<br>
<br>
코딩작업은 branch를 만들어서 작업하고<br>
절대 임의로 master branch를 수정하지 말것!!<br>
<br>
## 프로젝트 참여자
```
단국대학교 3학년 32141868 박유현<br>
단국대학교 3학년 32144697 최광진<br>
```

## 프로젝트 구성
본 프로젝트는 크게 세가지의 어플리케이션과 데이터베이스서버로 구성되어있음.<br>
### 1. SCV
관리자 및 사용자에게 UI를 제공해주기 위한 웹 어플리케이션
```
### 개발/실행 환경
- OS : Microsoft Windows 10
- Language : JRE 1.8
- IDE : Eclipse Jee Photon
- Web Server : Tomcat 8.0
```
### 2. EncodingServer
인코딩 연산을 진행하는 서버
```
### 개발/실행 환경
- OS : Microsoft Windows 10
- Language : JRE 1.8
- IDE : Eclipse Jee Photon
- Web Server : Tomcat 8.0
```
### 3. LearningServer
인코딩 연산 결과물을 분류 및 학습하는 서버
```
### 개발/실행 환경
- OS : 
- Language : Python
- IDE : 
- Web Server : 
```
### 4. 데이터베이스 서버
```
### 개발/실행 환경
- OS : Microsoft Windows 10
- DBMS : MySQL 8.0
- IP Address : 112.186.29.44
- Port : 3306
- DB name : 
```

## 이클립스 초기 개발환경 설정
```
1. 다운받은 Github repository 내 eclipse_workspace 폴더를 이클립스 workspace로 지정하여 실행
2. 이클립스 상단 메뉴 Window - Preferences로 설정 메뉴 진입하여 몇가지 설정작업 진행<br>
   2-1. General - Workspace 탭에서 Text file encoding을 UTF-8로 지정<br>
   2-2. Java - Installed JREs 탭에서 JRE 1.8을 설치한 경로 찾아 추가한 뒤 기본값으로 설정<br>
   2-3. Server - Runtime Environment 탭에서 Tomcat 8.0 설치한 경로 찾아 추가<br>
   2-4. Web - CSS Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정<br>
   2-5. Web - HTML Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정<br>
   2-6. Web - JSP Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정
3. 이클립스 Project Explorer에 프로젝트가 나타나지 않을 경우<br>
   3-1. 이클립스 상단 메뉴 File - import... 메뉴 진입<br>
   3-2. General - Existing Projects into Workspace 선택한 뒤 Next<br>
   3-3. Import source에 workspace 내의 프로젝트 폴더를 선택한 뒤 Finish
4. DBMS에 접속할 수 있도록 MySQL 8.0 전용 JDBC 커넥터를 Tomcat 8.0이 설치된 위치 - lib 폴더 안에 위치시킬것
```

## 사용중인 외부 API
```
### Back-end
- FFMPEG (https://www.ffmpeg.org/)
- COS.jar
- JSON-simple-1.1.1.jar
- juniversechardet-2.1.0.jar (https://github.com/albfernandez/juniversalchardet)
```
```
### Front-end
- jQuery (https://jquery.com/)
- Bootstrap (https://getbootstrap.com/)
- toastr (https://github.com/CodeSeven/toastr)
- jQuery-contextMenu (https://github.com/swisnl/jQuery-contextMenu)
- jQuery-bgswitcher (https://github.com/rewish/jquery-bgswitcher)
- Video.js (https://videojs.com/)
- svideojs-quality-selector (https://github.com/silvermine/videojs-quality-selector)
- AXICON (https://axisj.com/axicon/)
```
