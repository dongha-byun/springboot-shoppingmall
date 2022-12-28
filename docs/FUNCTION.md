# 쇼핑몰 API 개발 목록
### api 응답 기본 틀
#### response
##### 1. returnCode : SUCCESS / FAIL
##### 2. message : 웹페이지에 표시할 메세지 or alert 메세지
##### 3. data : 필요시 추가

## 1. 사용자 
### 1) 회원가입 요청 : /sign-up (POST) O

### 2) 로그인 요청 : /login (POST) O
#### * 세션에서 보관할 사용자 정보? => pk 만 일단
#### 2-1) JWT 인증처리 - 자체 로그인
#### 2-2) 카카오 로그인
#### 2-3) 네이버 로그인

### 3) ID 찾기 : /find-id (GET) O
### 4) PW 찾기 : /find-pw (GET) O
### 5) 기본 정보 조회 : /user/{id} (GET) O
### 6) 기본 정보 수정 : /user/{id} (PUT) O
### 7) 배송지 정보 조회 : /user/{id}/delivery (GET)
### 8) 결제수단 목록 조회 : /user/{id}/payment (GET)
### 9) 주문내역 조회 : /user/{id}/orders (GET)
### 10) 장바구니 목록 조회 : /user/{id}/basket (GET)
### 11) 장바구니 추가 : /user/{id}/baskets (POST)
### 12) 장바구니 제거 : /user/{id}/baskets/{id} (DELETE)

## 2. 상품 (구매자)
### 1) 상품 목록 조회 : /products (GET)
### 2) 상품 상세 조회 : /products/{id} (GET)
#### 2-1) 상품리뷰(review)
#### 2-2) 상품문의(qna)
#### 2-3) 상품상세정보(detail)

## 3. 상품 (판매자)
### 1) 상품 등록 : /products (POST)

## 4. 카테고리 (관리자)
### 1) 카테고리 등록 : /category (POST)
### 2) 카테고리 조회 : /category/{id} (GET)
### 3) 카테고리 수정 : /category/{id} (PUT)
### 4) 카테고리 삭제 : /category/{id} (DELETE)
