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
### 1) 상품 목록 조회 : /products?categoryId=&subCategoryId= (GET) O
### 2) 상품 상세 조회 : /products/{id} (GET)
#### 2-1) 상품리뷰(review)

#### 2-2) 상품문의(qna) : /products/{id}/qna (GET) , /products/{id}/qna/{qnaId} (GET), /products/{id}/qna (POST)
##### 1. 로그인한 사용자는 특정 상품에 문의글을 남길 수 있다.
#####    (비회원은 문의 못남김 -> 문의작성 시도 시, 로그인화면으로 이동하여 로그인 후 리다이렉트 처리) 
##### 2. 비밀글로 문의를 남긴 경우, 답변도 비밀글이 되고, 작성자가 작성 시 입력한 패스워드를 입력하면 문의글 / 답변글 모두 조회된다.

#### 2-2) 상품문의 답변(qnaAnswer)
##### 1. 상품의 판매자는 사용자들이 남긴 문의를 판매자 페이지에서 확인한다.
##### 2. 상품문의에 비방 및 욕설 등 부적절한 내용이 있는 경우, 판매자는 해당 문의를 신고할 수 있다.
##### 3. 답변이 완료되면 해당 문의의 답변은 문의글의 댓글형태로 조회된다.
##### 4. 신고된 문의는 관리자 페이지에서 확인 가능하고, 관리자가 해당 문의를 안보이게 처리할 수 있다.(이 때, 작성자에게 메일을 보낸다.)

#### 2-3) 상품상세정보(detail)

## 3. 상품 (판매자)
### 1) 상품 등록 : /products (POST) O

## 4. 카테고리 (관리자)
### 1) 카테고리 등록 : /category (POST)
### 2) 카테고리 조회 : /category/{id} (GET)
### 3) 카테고리 수정 : /category/{id} (PUT)
### 4) 카테고리 삭제 : /category/{id} (DELETE)
