package Choi.clean_lottery.infrastructure.role.area;

import Choi.clean_lottery.domain.role.area.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaJpaRepository extends JpaRepository<Area, Long> {
}
