package springboot.shoppingmall.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OrderSequenceRepositoryTest {

    @Autowired
    OrderSequenceRepository orderSequenceRepository;

    @Test
    @DisplayName("date 로 OrderSequence 조회")
    void find_by_date_test() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 9, 0, 0, 0);
        OrderSequence sequence = OrderSequence.createSequence(dateTime);
        orderSequenceRepository.save(sequence);

        // when
        String findDate = "20230509";
        Optional<OrderSequence> optionalDate = orderSequenceRepository.findByDate(findDate);

        // then
        assertThat(optionalDate.isPresent()).isTrue();

        OrderSequence findSequence = optionalDate.get();
        assertThat(findSequence.getDate()).isEqualTo(findDate);
        assertThat(findSequence.getSeq()).isEqualTo(0L);
    }
}