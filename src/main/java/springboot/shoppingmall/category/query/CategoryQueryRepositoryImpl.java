package springboot.shoppingmall.category.query;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final EntityManager entityManager;

    @Override
    public List<CategoryQueryDto> findCategoryAll() {
        return entityManager.createQuery(
                "select new springboot.shoppingmall.category.query.CategoryQueryDto(parent.id, parent.name, child.id, child.name) "
                + "from Category parent, Category child "
                + "where parent.id = child.parent.id", CategoryQueryDto.class)
                .getResultList();
    }
}
