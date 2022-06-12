package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.domain.team.Team;

public interface ChangeRoleTableReader {
    ChangeRoleTable getTableById(Long tableId);

    Long getTableIdByTeam(Team team);
}
