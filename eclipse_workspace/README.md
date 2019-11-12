## 이클립스로 개발중인 어플리케이션
- EncodingServer
- SCV

## 이클립스 초기 개발환경 설정
```
1. 다운받은 Github repository 내 eclipse_workspace 폴더를 이클립스 workspace로 지정하여 실행
2. 이클립스 상단 메뉴 Window - Preferences로 설정 메뉴 진입하여 몇가지 설정작업 진행
   2-1. General - Workspace 탭에서 Text file encoding을 UTF-8로 지정
   2-2. Java - Installed JREs 탭에서 JRE 1.8을 설치한 경로 찾아 추가한 뒤 기본값으로 설정
   2-3. Server - Runtime Environment 탭에서 Tomcat 8.0 설치한 경로 찾아 추가
   2-4. Web - CSS Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정
   2-5. Web - HTML Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정
   2-6. Web - JSP Files 탭에서 Encoding을 ISO 10646/Unicode(UTF-8)로 설정
3. 이클립스 Project Explorer에 프로젝트가 나타나지 않을 경우
   3-1. 이클립스 상단 메뉴 File - import... 메뉴 진입
   3-2. General - Existing Projects into Workspace 선택한 뒤 Next
   3-3. Import source에 workspace 내의 프로젝트 폴더를 선택한 뒤 Finish
4. DBMS에 접속할 수 있도록 MySQL 8.0 전용 JDBC 커넥터를 Tomcat 8.0이 설치된 위치 - lib 폴더 안에 위치시킬것
```
