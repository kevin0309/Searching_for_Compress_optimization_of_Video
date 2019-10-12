# EncodingServer
LearningServer에서 요청한 인코딩 작업을 처리하기 위한 어플리케이션

## 업데이트 로그
### 2019.10.01 - v0.1.0 u191001
```
- 프레임워크를 사용하지 않고 자체적으로 개발.
- 기초적인 사항 구현 (요청처리 서블릿, DB 맵핑 및 사용준비, 기타 필수요소들...)
```

### 2019.10.13 - v0.2.0 u191013
```
- 서버 상태를 DB에 실시간으로 연동하기 위하여 (온라인인지 오프라인인지) 로직 추가
  MAC주소를 사용하여 구현하였음.
- 서버에서 사용 가능한 HDD를 찾는 로직 추가
```