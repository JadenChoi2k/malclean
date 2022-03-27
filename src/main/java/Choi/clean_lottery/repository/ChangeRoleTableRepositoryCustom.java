package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.ChangeRoleTable;
import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;

public interface ChangeRoleTableRepositoryCustom {

    ChangeRoleTable saveByTeamAndRole(Team team, Role receiveRole);

    ChangeRoleTable saveByTeamAndRole(Long teamId, Long receiveRoleId);
}
