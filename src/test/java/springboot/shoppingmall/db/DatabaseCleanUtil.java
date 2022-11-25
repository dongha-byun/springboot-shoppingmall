package springboot.shoppingmall.db;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@ActiveProfiles("test")
public class DatabaseCleanUtil implements InitializingBean {

    @PersistenceContext
    protected EntityManager em;
    private List<String> tableNames;

    @Transactional
    public void cleanUp(){
        em.flush();

        // 외래키 제약 조건 해제
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        // 엔티티 all truncate
        for (String tableName : tableNames) {
            em.createNativeQuery("truncate table "+tableName).executeUpdate();
        }

        // 외래키 제약 조건 설정
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = em.getMetamodel().getEntities()
                .stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Table.class) != null)
                .map(entityType -> entityType.getJavaType().getAnnotation(Table.class).name())
                .collect(Collectors.toList());
    }
}