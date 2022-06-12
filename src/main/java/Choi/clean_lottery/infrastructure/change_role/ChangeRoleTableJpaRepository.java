package Choi.clean_lottery.infrastructure.change_role;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangeRoleTableJpaRepository extends JpaRepository<ChangeRoleTable, Long> {
    Optional<ChangeRoleTable> findFirstByTeamOrderByCreateDateDesc(Team team);
}
