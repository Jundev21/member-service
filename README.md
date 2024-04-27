# member service 

1. 회원등록 API - (/api/user/join)
2. 회원 로그인 API - (/api/user/login)
3. 회원 정보 수정 API - (/api/user/{loginId})
4. 회원 리스트 검색 API - (/api/user/list)

사용자에 관한 기본적인 회원등록,로그인, 회원정보 조회, 회원 정보 수정을 구성하였으며 Spring Security JWT 를 활용하여 로그인 회원가입을 구현하였습니다. 
회원가입을 거쳐 로그인을 할 시 JWT 토큰이 생성되며 토큰을 통하여 사용자 회원조회, 회원수정이 이뤄집니다.  

## 회원 서비스 파일 구조
Domain 기준으로 Controller , DTO, Entity, Repository, Service 형태로 파일을 구성하였습니다.  
```
├── main
│   ├── generated
│   ├── java
│   │   └── com
│   │       └── commerce
│   │           └── memberservice
│   │               ├── MemberServiceApplication.java
│   │               ├── common
│   │               │   ├── exception
│   │               │           └── securityException
│   │               │   ├── BasicResponse
│   │               ├── config
│   │               ├── domain
│   │               │   └── membmer
│   │               │      ├── controller
│   │               │      ├── dto
│   │               │         ├── Response
│   │               │         └── Request
│   │               │      ├── entity
│   │               │      ├── reposiroty
│   │               │      └── service
│   │               ├── filter
│   │               │   └── auth
│   │               └── jwt
│   └── resources
│       ├── application.yml
└── test
│   ├── java
│   │   └── com
│   │       └── commerce
│   │           └── memberservice
│   │               ├── domain
│   │               ├── controller
│   │               ├── repository
│   │               ├── service
│

```
## 프로젝트 구조
![Screenshot 2024-04-28 at 5 42 25 AM](https://github.com/YBEMiniProjectTeam/MINI-Back/assets/55421772/361159a6-2998-4498-b90b-85400048abf8)

## 개발 환경
- 개발 언어: `Java 8`
- 개발 환경 : `Spring Boot 2.7.0` , `Gradle`
    - 라이브러리 / 의존성 : `Spring Web` , `Spring Security`, `JUnit`, `JWT Token`, `JPA`, `Lombok`, `H2` , `Swagger`, `Validation`, `Mysql` 
- 로컬 Swagger URL : http://localhost:8080/swagger-ui/index.html
- 기능 :
    - 로그인
    - 회원가입
    - 회원정보 수정
    - 회원정보 조회
    - 필터링 커스텀
    - 응답 및 에러핸들링
    - 
  
## 포스트맨

- 회원등록
<img width="1303" alt="Screenshot 2024-04-28 at 5 58 21 AM" src="https://github.com/WePlanPlans/WPP_BE/assets/55421772/87a3336f-17c3-429c-8a28-303f6d7296c5">

- 로그인
<img width="1299" alt="Screenshot 2024-04-28 at 5 58 31 AM" src="https://github.com/WePlanPlans/WPP_BE/assets/55421772/4ac9cbda-e87e-4013-b167-04bb5eda6aa9">

- 회원정보 조회
<img width="1303" alt="Screenshot 2024-04-28 at 5 58 52 AM" src="https://github.com/WePlanPlans/WPP_BE/assets/55421772/506ea580-efa1-4dd9-af17-daa2c47a87c1">

- 회원정보 수정
<img width="1306" alt="Screenshot 2024-04-28 at 5 59 46 AM" src="https://github.com/WePlanPlans/WPP_BE/assets/55421772/9437ae1a-f698-4674-aff4-33e8d3e1c84a">
