package springboot.shoppingmall.product.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductQnaAnswerCompleteType {
    N("답변 미등록"),
    Y("답변 완료"),
    ALL("전체");

    private final String typeName;
}
