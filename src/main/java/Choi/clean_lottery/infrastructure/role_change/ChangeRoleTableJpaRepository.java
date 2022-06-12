package Choi.clean_lottery.infrastructure.role_change;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangeRoleTableJpaRepository extends JpaRepository<ChangeRoleTable, Long> {
    Optional<ChangeRoleTable> findFirstByTeamOrderByCreateDateDesc(Team team);
}
