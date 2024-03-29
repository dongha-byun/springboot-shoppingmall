package springboot.shoppingmall.category.query;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.dto.CategoryDto;


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

    @Override
    public List<CategoryDto> findParentsDto() {
        return entityManager.createQuery(
                        "select new springboot.shoppingmall.category.dto.CategoryDto(id, name, parent.id) "
                                + "from Category "
                                + "where parent is null "
                                + "order by id", CategoryDto.class)
                .getResultList();
    }

    @Override
    public List<CategoryDto> findSubCategoryDto(List<Long> parentIds) {
        return entityManager.createQuery(
                        "select new springboot.shoppingmall.category.dto.CategoryDto(id, name, parent.id) "
                                + "from Category "
                                + "where parent.id in :parentIds "
                                + "order by id", CategoryDto.class)
                .setParameter("parentIds", parentIds)
                .getResultList();
    }
}
