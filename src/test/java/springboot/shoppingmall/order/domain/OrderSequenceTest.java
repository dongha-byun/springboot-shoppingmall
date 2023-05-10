package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderSequenceTest {

    @Test
    @DisplayName("년월일 과 seq 정보를 관리한다.")
    void create_order_sequence_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 9, 0, 0, 0);

        // when
        OrderSequence sequence = OrderSequence.createSequence(dateTime);

        // then
        assertThat(sequence).isNotNull();
        assertThat(sequence.getSeq()).isEqualTo(0);
        assertThat(sequence.getDate()).isEqualTo("20230509");
    }

    @Test
    @DisplayName("주문 코드를 생성하면, 다음 SEQ 에 대한 주문 코드가 생성된다. ")
    void generate_order_code_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 9, 0, 0, 0);
        OrderSequence sequence = OrderSequence.createSequence(dateTime);
        assertThat(sequence.getSeq()).isEqualTo(0L);

        // when
        String orderCode = sequence.generateOrderCode();

        // then
        assertThat(orderCode).isEqualTo("20230509000000001");
    }

    @Test
    @DisplayName("새로 생성한 SEQ 인지 판단한다.")
    void new_order_sequence_check_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 9, 0, 0, 0);
        OrderSequence sequence = OrderSequence.createSequence(dateTime);
        assertThat(sequence.getSeq()).isEqualTo(0L);

        // when
        boolean isNew = sequence.isNew();

        // then
        assertThat(isNew).isTrue();
    }
}