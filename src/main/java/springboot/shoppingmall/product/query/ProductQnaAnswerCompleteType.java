package springboot.shoppingmall.product.query;

public enum ProductQnaAnswerCompleteType {
    N("답변 미등록"),
    Y("답변 완료"),
    ALL("전체");

    private String typeName;

    ProductQnaAnswerCompleteType(String typeName) {
        this.typeName = typeName;
    }
}
