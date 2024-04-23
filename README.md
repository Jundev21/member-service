# member API service 

1. 회원등록 API
2. 회원 정보 수정 API
3. 회원 리스트 검색 API

회원 API 을 작성하기위해서 회원 정보를 인증이 필요하기때문에 이런 부분은 JWT 를 활용하였습니다. 

## 회원 서비스 파일 구조

크게 Domain을 기준으로 나눠서 파일구조를 작성했습니다.
```
├── main
│   ├── generated
│   ├── java
│   │   └── com
│   │       └── commerce
│   │           └── memberservice
│   │               ├── MemberServiceApplication.java
│   │               ├── common
│   │               │   ├── BasicException.java
│   │               │   └── BasicResponse.java
│   │               ├── config
│   │               │   ├── JwtConfig.java
│   │               │   ├── PasswordEncoderConfig.java
│   │               │   └── SwaggerConfig.java
│   │               ├── domain
│   │               │   └── membmer
│   │               │      ├── controller
│   │               │      ├── dto
│   │               │         ├── Response
│   │               │         └── Request
│   │               │      ├── entity
│   │               │      ├── reposiroty
│   │               │      └── service
│   └── resources
│       ├── application.yml
└── test
```
