## SpringBoot 와 JPA 를 활용한 온라인 쇼핑몰 플랫폼

### 0. 소개
* 커머스 도메인 설계 및 구현을 위한 온라인 쇼핑몰 플랫폼 프로젝트
* SpringBoot 와 JPA 학습을 중심으로 한 백엔드 서비스 개발
* JUnit 을 통한 단위/통합 테스트 코드 작성
* Spring Cloud 를 활용한 MSA 구현

### 1. 개발 환경
* Java 11 / 17
* SpringBoot 2.7.4 / SpringBoot 3.1.5
* Spring Cloud 4.0(Netflix Eureka / OpenFeign)
* Spring Data JPA / QueryDSL
* MariaDB
* JUnit 5

### 2. ERD
![ERD](https://github.com/dongha-byun/springboot-shoppingmall/assets/95368245/5d70c868-e300-4e38-93f5-70f4b85c046d)

### 3. 소프트웨어 아키텍처
![SW_ARCH](https://github.com/dongha-byun/springboot-shoppingmall/assets/95368245/4ce7f5dd-f0f2-4b91-8511-8300cb8b75e8)

### 4. 주요 구현기능
#### 1) 메인 쇼핑몰 서비스
* 카카오 페이 결제 기능
* 다수의 상품을 한 번에 주문하는 기능
* 주문 상태에 따른 주문취소, 환불, 교환 기능
* 주문 상품 결제 시, 할인쿠폰 사용 기능
* 주문 건수, 주문 금액에 따른 회원등급 적용 및 회원등급에 따른 결제금액 할인 기능
* 쇼핑몰 입점신청 기능(판매자 전용)
* 웹 에디터를 통한 판매 상품 정보 등록 기능(판매자 전용)
* 상품 리뷰/문의 글에 대한 신고 기능(판매자 전용)
* 입점 승인 및 판매자격 중단(관리자 전용)
* 상품 리뷰/문의 신고에 대한 사후 처리(관리자 전용)

#### 2) 회원 / 인증 서비스
* 로그인 시, JWT 토큰 방식 인증 처리
* Access Token 발급 및 Refresh Token 을 통한 Access Token 재발급 기능
* 회원가입 시, 이메일 발송을 통한 인증번호 검증 기능

#### 3) API Gateway
* 요청 URI 에 따른 라우팅 처리 및 필터 적용
* 요청 내 Access Token 을 검증하여 인증 처리


