package springboot.shoppingmall.partners.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByCrn(String crn);
}
