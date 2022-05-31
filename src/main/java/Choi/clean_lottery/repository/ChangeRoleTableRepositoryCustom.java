package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;

public interface ChangeRoleTableRepositoryCustom {

    ChangeRoleTable saveByTeamAndRole(Team team, Role receiveRole);

    ChangeRoleTable saveByTeamAndRole(Long teamId, Long receiveRoleId);
}
