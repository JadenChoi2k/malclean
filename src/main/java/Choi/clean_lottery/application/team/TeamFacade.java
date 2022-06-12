package Choi.clean_lottery.application.team;

import Choi.clean_lottery.domain.team.TeamCommand;
import Choi.clean_lottery.domain.team.TeamInfo;
import Choi.clean_lottery.domain.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamFacade {
    private final TeamService teamService;

    public TeamInfo registerTeam(TeamCommand.RegisterTeam registerTeamRequest) {
        return teamService.registerTeam(registerTeamRequest);
    }

    public TeamInfo addMemberToTeam(TeamCommand.AddMemberRequest addMemberRequest) {
        return teamService.addMemberToTeam(addMemberRequest);
    }

    public void changeCurrentRole(TeamCommand.ChangeCurrentRoleRequest changeCurrentRoleRequest) {
        teamService.changeCurrentRole(changeCurrentRoleRequest);
    }

    public TeamInfo changeManager(TeamCommand.ChangeManagerRequest changeManagerRequest) {
        return teamService.changeManager(changeManagerRequest);
    }

    public TeamInfo retrieveTeamInfo(Long teamId) {
        return teamService.retrieveTeamInfo(teamId);
    }
}
