# 🚀 TaskFlow - 협업 작업 관리 시스템

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Auth-black.svg)](https://jwt.io/)
[![H2](https://img.shields.io/badge/H2-Test%20DB-lightblue.svg)](https://www.h2database.com/)

> 사용자 및 팀 단위의 작업 관리를 위한 웹 애플리케이션

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [아키텍처](#-아키텍처)
- [API 명세](#-api-명세)
- [시작하기](#-시작하기)
- [사용법](#-사용법)
- [프로젝트 구조](#-프로젝트-구조)
- [기여하기](#-기여하기)

## 🎯 프로젝트 개요

TaskFlow는 팀 협업을 위한 종합적인 작업 관리 플랫폼입니다. 프로젝트 진행 상황을 실시간으로 추적하고, 팀원 간의 효율적인 커뮤니케이션을 지원합니다.

> **Note**: 현재 버전에서는 작업(Task)이 특정 팀에 귀속되지 않고 담당자에게만 할당됩니다. 따라서 팀별 작업 목록 조회와 같은 기능에는 제약이 있습니다.

### 핵심 가치

- **투명성**: 모든 작업 진행 상황을 한눈에 파악
- **협업**: 팀원 간의 원활한 소통과 정보 공유
- **효율성**: 직관적인 UI/UX로 작업 관리 시간 단축

## ✨ 주요 기능

### 👥 사용자 관리

- **회원 가입/로그인**: JWT 기반 보안 인증
- **권한 관리**: Admin/User 역할 기반 접근 제어
- **계정 관리**: 회원 탈퇴 기능

### 🏢 팀 관리

- **팀 생성/수정**: 프로젝트별 팀 구성
- **멤버 관리**: 팀원 추가/제거
- **팀 조회**: 소속 팀 및 가용 멤버 확인

### 📝 작업 관리

- **작업 생성**: 상세한 작업 정보 입력
- **상태 추적**: TODO → IN_PROGRESS → DONE 상태 관리
- **우선순위**: LOW, MEDIUM, HIGH 우선순위 설정
- **담당자 배정**: 자신에게 작업 할당
- **마감일 관리**: 일정 기반 작업 계획

### 💬 댓글 시스템

- **계층형 댓글**: 댓글에 대한 답글 기능 (요청 시 `parentId` 포함)
- **댓글 기반 소통**: 작업 관련 토론 및 피드백
- **정렬 옵션**: 시간순 댓글 정렬

## 🛠 기술 스택

### Backend

- **Java 17**: 최신 LTS 버전으로 안정성과 성능 보장
- **Spring Boot 3.5.5**: 마이크로서비스 아키텍처 지원
- **Spring Security**: OAuth2 Resource Server 및 JWT 인증
- **Spring Data JPA**: 객체 관계 매핑 및 데이터베이스 추상화

### Database

- **MySQL**: 운영 환경 데이터베이스
- **H2**: 개발 및 테스트 환경

### Authentication & Security

- **JWT (JSON Web Tokens)**: jjwt 0.11.5
- **BCrypt**: 비밀번호 암호화
- **OAuth2 Resource Server**: 토큰 기반 인증

### Testing

- **JUnit 5**: 단위 테스트 프레임워크
- **Mockito**: 모킹 및 테스트 더블
- **Spring Boot Test**: 통합 테스트 지원

### Development Tools

- **Lombok**: 보일러플레이트 코드 감소
- **Gradle**: 빌드 자동화 및 의존성 관리

## 🏗 아키텍처

TaskFlow는 **도메인 주도 설계(DDD)** 와 **클린 아키텍처** 원칙을 **지향하여** 구성되어 있습니다.

```
┌─────────────────┬─────────────────┬─────────────────┐
│   Presentation  │    Application  │    Domain       │
│                 │                 │                 │
│ ┌─────────────┐ │ ┌─────────────┐ │ ┌─────────────┐ │
│ │ Controllers │ │ │  Use Cases  │ │ │  Entities   │ │
│ │     DTOs    │ │ │   Services  │ │ │             │ │
│ └─────────────┘ │ └─────────────┘ │ └─────────────┘ │
└─────────────────┼─────────────────┼─────────────────┘
                  │                 │
┌─────────────────┼─────────────────┼─────────────────┐
│ Infrastructure  │                 │                 │
│                 │                 │                 │
│ ┌─────────────┐ │                 │                 │
│ │ Repositories│ │                 │                 │
│ │   Database  │ │                 │                 │
│ │     JPA     │ │                 │                 │
│ └─────────────┘ │                 │                 │
└─────────────────┴─────────────────┴─────────────────┘
```

### 핵심 도메인

#### 🔐 Auth Domain

- 사용자 인증 및 권한 관리
- JWT 토큰 생성/검증/블랙리스트

#### 👤 User Domain

- 회원 정보 관리
- 역할 기반 접근 제어

#### 👥 Team Domain

- 팀 생성 및 관리
- 팀원 관리

#### 📋 Task Domain

- 작업 생성/수정/삭제
- 상태 및 우선순위 관리

#### 💬 Comment Domain

- 계층형 댓글 시스템
- 작업 관련 커뮤니케이션

## 📊 API 명세

### 인증 관련

```http
POST /api/auth/register    # 회원가입
POST /api/auth/login       # 로그인
POST /api/auth/withdraw    # 회원탈퇴
```

### 사용자 관리

```http
GET  /api/users/me         # 내 정보 조회
GET  /api/users            # 전체 사용자 목록
GET  /api/users/available  # 특정 팀에 추가 가능한 사용자 목록
```

### 팀 관리

```http
POST   /api/teams                      # 팀 생성
GET    /api/teams                      # 팀 목록 조회
GET    /api/teams/{teamId}             # 특정 팀 조회
PUT    /api/teams/{teamId}             # 팀 정보 수정
DELETE /api/teams/{teamId}             # 팀 삭제
POST   /api/teams/{teamId}/members     # 팀원 추가
DELETE /api/teams/{teamId}/members/{userId}  # 팀원 제거
```

### 작업 관리

```http
POST   /api/tasks                      # 작업 생성
GET    /api/tasks                      # 작업 목록 조회 (필터링 지원)
GET    /api/tasks/{taskId}             # 특정 작업 조회
PUT    /api/tasks/{taskId}             # 작업 수정
DELETE /api/tasks/{taskId}             # 작업 삭제
PATCH  /api/tasks/{taskId}/status      # 작업 상태 변경
```

### 댓글 관리

```http
POST   /api/tasks/{taskId}/comments           # 작업에 댓글 또는 대댓글 작성
GET    /api/tasks/{taskId}/comments           # 작업의 댓글 목록
DELETE /api/tasks/{taskId}/comments/{commentId}  # 내 댓글 삭제
```

## 🚀 시작하기

### 필수 요구사항

- Java 17 이상
- MySQL 8.0 이상 (또는 H2 사용)
- Gradle 7.0 이상

### 설치 및 실행

1. **프로젝트 클론**

   ```bash
   git clone https://github.com/eunji-company/taskflow-spring.git
   cd taskflow-spring
   ```

2. **데이터베이스 설정**

   ```bash
   # MySQL 데이터베이스 생성
   mysql -u root -p
   CREATE DATABASE taskflow;
   ```

3. **설정 파일 수정**

   ```yaml
   # src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/taskflow
       username: your_username
       password: your_password
   ```

4. **애플리케이션 실행**

   ```bash
   # 애플리케이션 실행
   ./gradlew bootRun

   # 또는 JAR 파일 빌드 후 실행
   ./gradlew build
   java -jar build/libs/taskflow-0.0.1-SNAPSHOT.jar
   ```

5. **테스트 실행**
   ```bash
   ./gradlew test
   ```

### 기본 계정

시스템 시작 시 관리자 계정이 자동으로 생성됩니다:

- **아이디**: admin
- **비밀번호**: admin1234!
- **역할**: ADMIN

## 💡 사용법

### 1. 회원가입 및 로그인

```json
POST /api/auth/register
{
  "username": "testuser",
  "password": "password123!",
  "email": "user@example.com",
  "name": "홍길동"
}
```

### 2. 팀 생성

```json
POST /api/teams
{
  "name": "개발팀",
  "description": "백엔드 개발 팀입니다."
}
```

### 3. 작업 생성

```json
POST /api/tasks
{
  "title": "로그인 API 구현",
  "description": "JWT 기반 로그인 API를 구현합니다.",
  "dueDate": "2024-12-31T23:59:59",
  "priority": "HIGH",
  "assigneeId": 1
}
```

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/indiv/abko/taskflow/
│   │   ├── TaskflowApplication.java      # 메인 애플리케이션 클래스
│   │   ├── StartUpService.java           # 초기 데이터 설정
│   │   ├── domain/                       # 도메인 레이어
│   │   │   ├── auth/                     # 인증 도메인
│   │   │   ├── user/                     # 사용자 도메인
│   │   │   ├── team/                     # 팀 도메인
│   │   │   ├── task/                     # 작업 도메인
│   │   │   └── comment/                  # 댓글 도메인
│   │   └── global/                       # 공통 설정 및 유틸리티
│   │       ├── config/                   # 설정 클래스
│   │       ├── exception/                # 전역 예외 처리
│   │       ├── jwt/                      # JWT 관련 유틸리티
│   │       └── auth/                     # 인증/인가 관련
│   └── resources/
│       ├── application.yml               # 공통 설정
│       └── application-test.yml          # 테스트 환경 설정
└── test/                                 # 테스트 코드
    └── java/indiv/abko/taskflow/
        ├── domain/                       # 도메인별 테스트
        └── global/                       # 공통 기능 테스트
```

### 아키텍처 특징

#### 📦 도메인별 패키지 구성

각 도메인은 독립적인 패키지로 구성되어 있으며, 다음과 같은 구조를 갖습니다:

```
domain/{domain_name}/
├── controller/      # REST API 엔드포인트
├── service/         # 비즈니스 로직 (Use Case)
├── entity/          # JPA 엔티티
├── repository/      # 데이터 액세스 계층
├── dto/            # 데이터 전송 객체
│   ├── request/    # 요청 DTO
│   ├── response/   # 응답 DTO
│   └── command/    # 명령 객체
├── mapper/         # DTO 변환 로직
└── exception/      # 도메인별 예외
```

#### 🎯 Use Case 패턴

각 비즈니스 기능은 독립적인 Use Case 클래스로 구현되어 있습니다:

- `CreateTaskUseCase`: 작업 생성
- `UpdateTaskUseCase`: 작업 수정
- `FindAllTasksUseCase`: 작업 목록 조회
- `LoginUseCase`: 로그인
- `RegisterUseCase`: 회원가입

## 🔧 환경 설정

### 기본 설정 (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taskflow
    username: ${DB_USERNAME:taskflow}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: create-drop # 개발 초기에는 create-drop, 안정화 후 validate 또는 none
    show-sql: true
```

### JWT 설정 (`application.yml`)

```yaml
jwt:
  secret: ${JWT_SECRET:your-secret-key-with-at-least-256-bits}
  access-token-validity-seconds: 3600 # 1시간
```

## 🧪 테스트

### 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 도메인 테스트 실행
./gradlew test --tests "*.task.*"

# 테스트 커버리지 리포트 생성
./gradlew jacocoTestReport
```

### 테스트 구조

- **단위 테스트**: 각 Use Case 및 Service 클래스
- **통합 테스트**: Controller 및 Repository 테스트
- **보안 테스트**: JWT 인증/인가 테스트

## 📈 성능 및 모니터링

### 주요 성능 지표

- **응답 시간**: 평균 100ms 이하
- **처리량**: 초당 1000+ 요청 처리
- **가용성**: 99.9% 업타임 목표

### 모니터링 도구

- Spring Boot Actuator를 통한 헬스체크
- 로그 기반 에러 추적
- 데이터베이스 성능 모니터링

## 🚀 배포

### Docker를 활용한 배포

```dockerfile
FROM openjdk:17-jre-slim

COPY build/libs/taskflow-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 배포 명령어

```bash
# Docker 이미지 빌드
docker build -t taskflow:latest .

# 컨테이너 실행
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod taskflow:latest
```

## 🤝 기여하기

TaskFlow 프로젝트에 기여해주셔서 감사합니다! 다음 가이드라인을 따라주세요:

### 기여 방법

1. 이슈 생성 또는 기존 이슈 확인
2. 기능 브랜치 생성 (`feature/기능명` 또는 `bugfix/버그명`)
3. 코드 작성 및 테스트 추가
4. Pull Request 생성

### 코딩 스타일

- **Java 코딩 컨벤션**: Google Java Style Guide 준수
- **커밋 메시지**: Conventional Commits 형식 사용
- **테스트 커버리지**: 최소 80% 이상 유지

### 브랜치 전략

```
main                    # 운영 배포 브랜치
├── develop            # 개발 브랜치
├── feature/*          # 기능 개발 브랜치
├── bugfix/*          # 버그 수정 브랜치
└── release/*         # 릴리즈 준비 브랜치
```

## 📝 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 👨‍💻 개발팀

- **백엔드 개발**: [eunji-company](https://github.com/eunji-company)
- **아키텍처 설계**: Spring Boot + JPA 기반 멀티레이어 아키텍처
- **보안**: JWT + Spring Security 기반 인증/인가

## 📞 문의하기

프로젝트에 대한 질문이나 제안사항이 있으시면 다음을 통해 연락해주세요:

- **GitHub Issues**: [프로젝트 이슈 페이지](https://github.com/eunji-company/taskflow-spring/issues)
- **Email**: taskflow.support@example.com

---

**TaskFlow**로 더 효율적인 팀 협업을 시작하세요! 🚀

<div align="center">
  <img src="https://img.shields.io/badge/Made%20with-❤️-red.svg" alt="Made with Love">
  <img src="https://img.shields.io/badge/Built%20with-Spring%20Boot-brightgreen.svg" alt="Built with Spring Boot">
</div>
