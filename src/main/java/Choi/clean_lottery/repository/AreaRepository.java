package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long>, AreaRepositoryCustom {
}
