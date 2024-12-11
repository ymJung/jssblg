# 자소설 공고 기반 블로그 글 발행 시스템 (Spring Boot 버전)

## 프로젝트 개요
이 프로젝트는 **자소설 사이트(https://jasoseol.com/)**의 공고 데이터를 수집하고, **네이버 블로그**의 메인 화면에서 공고 데이터를 수집한 뒤, 조회수(`view_count`)와 지원자 이력 수(`resume_count`)가 일정 기준 이상인 공고를 자동 선별하여 **ChatGPT API**를 활용해 네이버 블로그에 비공개 글로 발행하는 시스템입니다.  
본 시스템은 Java 기반 Spring Boot 프레임워크를 활용하여 백엔드 서버를 구성하며, REST API 형태로 제공됩니다.  
추후 별도의 프론트엔드(React, Vue, Angular 등)나 어드민 페이지를 연동할 수 있으며, 관리자가 공고 데이터를 확인하고 수동으로 블로그 글을 검토, 관리할 수 있습니다.

---

## 주요 기능
1. **데이터 수집**:
   - Spring Scheduler 또는 Cron Job을 활용하여 주기적으로 자소설 사이트에서 공고 데이터를 수집.
2. **조건 필터링**:
   - `view_count`와 `resume_count`가 100 이상인 공고만 처리.
3. **중복 처리 방지**:
   - 기존에 발행된 공고(`job_id`)를 DB에서 확인하여 중복 발행 방지.
4. **ChatGPT API 연동**:
   - 조건에 맞는 공고 데이터를 기반으로 블로그용 게시글 자동 생성.
5. **네이버 블로그 API 연동**:
   - 생성된 글을 네이버 블로그에 비공개로 발행.
6. **관리 인터페이스(선택 사항)**:
   - REST API를 통해 공고 데이터 조회, 수동 발행 및 관리 기능 제공 (추후 별도 프론트엔드 구성 가능).

---

## 기술 스택
### **백엔드**
- **언어**: Java 21
- **프레임워크**: Spring Boot (Web, Data JPA, Scheduling)
- **의존성 관리**: Gradle
- **HTTP 클라이언트**: RestTemplate 또는 WebClient
- **HTML 파싱**: Jsoup
- **로깅**: SLF4J + Logback

### **데이터 관리**
- **DBMS**: MySQL (InnoDB)
- **ORM**: Spring Data JPA

### **외부 API**
- **OpenAI API**: ChatGPT를 활용해 블로그 글 생성.
- **네이버 블로그 Open API**: 블로그 글 발행.

### **배포 및 운영**
- **서버 및 호스팅**: AWS EC2, Docker 등
- **로그 관리**: Logback, (필요 시 APM 및 Sentry)

---

## 데이터베이스 설계
**테이블 이름**: `job_postings`

| 컬럼 이름       | 타입           | 설명                     |
|----------------|---------------|------------------------|
| `id`           | BIGINT (PK)   | 기본 키 (Auto Increment) |
| `job_id`       | VARCHAR(255)  | 공고 ID (중복 방지)       |
| `company_name` | VARCHAR(255)  | 회사 이름                |
| `view_count`   | INT           | 조회수                   |
| `resume_count` | INT           | 이력서 수                |
| `created_at`   | DATETIME      | 등록일                  |
| `updated_at`   | DATETIME      | 수정일                  |

---

## 주요 흐름
### 1. 데이터 수집
- **Spring Scheduler**를 이용하여 일정 주기(Cron)로 자소설 사이트에서 데이터 수집.
- `RestTemplate` 또는 `Jsoup`을 활용하여 HTML 파싱 및 데이터 추출.
- 추출된 공고 데이터를 DB에 저장.

### 2. 조건 필터링 및 처리
- `/api/jobs` 엔드포인트를 통해 `view_count`와 `resume_count`가 100 이상인 공고만 필터링.
- 조건을 만족하는 공고를 대상으로 ChatGPT API 호출하여 블로그 게시글 초안 생성.

### 3. 블로그 글 발행
- 생성된 게시글 초안을 네이버 블로그 API를 통해 비공개로 발행.
- 발행 후 해당 `job_id`를 DB에 기록하여 중복 발행 방지.

---

## 설치 및 실행
### 1. 프로젝트 클론
```bash
git clone https://github.com/ymJung/jssblg.git
cd job-posting-blog-project
