package Choi.clean_lottery.infrastructure.team;

import Choi.clean_lottery.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamJpaRepository extends JpaRepository<Team, Long> {
}
