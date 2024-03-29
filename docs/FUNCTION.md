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
#### 2-1) JWT 인증처리 - 자체 로그인 O
#### 2-2) 카카오 로그인
#### 2-3) 네이버 로그인

### 3) ID 찾기 : /find-id (GET) O
### 4) PW 찾기 : /find-pw (GET) O
### 5) 기본 정보 조회 : /user (GET) O
### 6) 기본 정보 수정 : /user (PUT) O
### 7) 배송지 정보 조회 : /user/delivery (GET) O
### 8) 결제수단 목록 조회 : /user/payment (GET)
### 9) 주문내역 조회 : /user/orders (GET) O
### 10) 장바구니 목록 조회 : /user/basket (GET) O
### 11) 장바구니 추가 : /user/baskets (POST) O
### 12) 장바구니 제거 : /user/baskets/{id} (DELETE) O

## 2. 상품 (구매자)
### 1) 상품 목록 조회 : /products?categoryId=&subCategoryId= (GET) O
### 2) 상품 상세 조회 : /products/{id} (GET)
#### 2-1) 상품리뷰(review)

#### 2-2) 상품문의(qna) : /products/{id}/qna (GET) O
####                    /products/{id}/qna/{qnaId} (GET) O
####                    /products/{id}/qna (POST) O
##### 1. 로그인한 사용자는 특정 상품에 문의글을 남길 수 있다.
#####    (비회원은 문의 못남김 -> 문의작성 시도 시, 로그인화면으로 이동하여 로그인 후 리다이렉트 처리) 
##### 2. 비밀글로 문의를 남긴 경우, 답변도 비밀글이 되고, 작성자가 작성 시 입력한 패스워드를 입력하면 문의글 / 답변글 모두 조회된다.
##### 3. 문의글에 대한 조회는 로그인 여부 상관없이 모두가 볼 수 있다. ( 답변도 마찬가지 )

#### 2-2) 상품문의 답변(qnaAnswer) :  /products/{id}/qna/{qnaId}/answer (get)
####                               /products/{id}/qna/{qnaId}/answer (post)
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

## 5. 주문
### 1) 주문 1차 흐름(준비중 ~ 배송완료)
#### 1-1) 상품 1개와 갯수를 지정한다. - 완료
#### 1-2) 기존에 등록해놓은 배송지 중 상품이 배송될 배송지를 지정한다. - 완료
#### 1-3) 최초 주문이 생성되면 주문상태는 준비중(READY) 이 된다. - 완료

#### 1-4) 구매자는 마이페이지 > 주문내역 화면에서 주문 건에 대한 현재 상태를 조회한다.
##### 1-4-1) 구매자는 주문 후, 주문상태가 준비중이면 주문을 취소할 수 있다. - 완료
##### 1-4-2) 주문을 취소하면, 결제금액이 반환되며, 주문 상태가 취소(CANCEL) 이 된다. - 완료

#### 1-5) 판매자는 상품관리 > 주문/배송 화면에서 주문 목록을 조회한다.

#### 1-6) 판매자는 주문 목록에서 주문접수를 진행한다.
##### 1-6-1) 주문 접수는 배송을 하겠다는 의미로, 주문상태가 출고중(OUTING)이 된다. - 완료
##### 1-6-2) 주문이 접수되면 택배사를 통해 송장번호를 발부받고 송장을 출력한다.(실제 출력 로직은 개발하지 않는다.) - 완료
##### 1-6-3) 송장 번호 발급 과정 - 완료
> 1. 배송정보와 판매정보를 택배사 중계 서비스에 전달
> 2. 임의의 송장번호 발급, 송장번호 + 수령인 정보 + 판매자 정보 를 송장 정보 모두 포함
> 3. 판매정보와 배송정보 포함해서 송장번호 받아봄

##### 1-6-4) 출고 중 상태의 상품을 실제로 택배사에서 수거해가면, 상태를 배송중 으로 변경한다. - 완료

#### 1-7) 구매자에게 배송이 완료되면, 택배사는 배송완료 처리를 한다. - 완료
##### 1-7-1) 배송완료 처리 요청이 들어오면, 해당 주문의 주문상태는 배송완료(END)가 된다. - 완료
#### 1-8) 구매자가 배송된 상품을 확인하고 이상이 없는 경우, 구매확정(FINISH) 처리를 할 수 있다.
##### 1-8-1) 구매자가 구매확정한 상품은 환불/교환 을 신청할 수 없다.

### 2) 주문 2차 흐름(환불요청 ~ 완료)
#### 2-1) 구매자는 배송이 완료된 상품에 대해 환불을 요청 할 수 있다.
##### 2-1-1) 환불을 요청한 경우, 주문상태는 환불요청(RETURN_REQ)이 된다.
##### 2-1-2) 환불요청 상태가 들어오면, 배송을 담당했던 택배사에 상품 수거가 요청된다.
##### 2-1-3) 택배사가 상품을 수거해서 판매처에 배송한다.
#### 2-2) 판매자가 환불 상품을 수령하면 검수 시작을 처리한다. 
##### 2-2-1) 검수 시작 처리를 하면 주문 상태가 검수중(CHECKING) 으로 변경된다.
#### 2-3) 판매자가 상품의 검수를 끝내면, 검수 완료 처리를 한다.
##### 2-3-1) 검수 완료 처리를 하면, 주문 상태가 환불 완료(RETURN_END)로 변경된다.
##### 2-3-2) 주문 상태가 환불 완료(RETURN_END)로 변경되면, 구매자에게 구매 금액을 환불처리 한다.

### 3) 주문 3차 흐름(교환요청 ~ 배송완료)
#### 2-1) 구매자는 배송이 완료된 상품에 대해 교환을 요청할 수 있다.
##### 2-1-1) 교환을 요청한 경우, 다시 받을 상품의 옵션을 지정한다. (갯수 변경 불가)
##### 2-1-2) 상품의 옵션을 선택하고 교환 신청을 하면 주문상태가 교환 요청(EXCHANGE_REQ)로 변경된다.
##### 2-1-3) 주문 상태가 교환 요청으로 변경되면 배송한 택배사에 상품 수거 요청을 한다.
#### 2-2) 판매자가 상품을 수거하면 검수 시작 처리를 한다.
##### 2-2-1) 검수 시작 처리를 하면 주문 상태가 검수중(CHECKING)으로 변경된다.
#### 2-3) 판매자가 상품의 검수를 끝내면 검수 완료 처리를 한다.
##### 2-3-1) 검수 완료 처리를 하면 구매자가 지정한 옵션의 상품을 재발송한다.
##### 2-3-2) 재발송 처리 시, 택배사를 통해 송장번호를 부여받고 송장을 출력한다.  
##### 2-3-3) 이 때, 주문의 상태는 배송중(DELIVERY)이 된다.
#### 이후 과정은 주문 1차 흐름의 1-7) ~ 1-8) 까지 동일하다.