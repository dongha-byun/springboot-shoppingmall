package springboot.shoppingmall.category.query;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategoryQueryRepositoryTest {

    @Autowired
    CategoryQueryRepository categoryQueryRepository;

    @Test
    @DisplayName("카테고리 쿼리 테스트")
    void findCategoryAll(){
        List<CategoryQueryDto> categoryAll = categoryQueryRepository.findCategoryAll();

        assertThat(categoryAll).hasSize(39);
    }
}