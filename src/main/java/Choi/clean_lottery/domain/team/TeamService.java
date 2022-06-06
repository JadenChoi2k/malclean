package Choi.clean_lottery.domain.team;

public interface TeamService {
    TeamInfo registerTeam(TeamCommand.RegisterTeam registerTeamRequest);

    TeamInfo addMemberToTeam(TeamCommand.AddMemberRequest addMemberRequest);

    void changeCurrentRole(TeamCommand.ChangeCurrentRoleRequest changeCurrentRoleRequest);

    TeamInfo changeManager(TeamCommand.ChangeManagerRequest changeManagerRequest);

    TeamInfo retrieveTeamInfo(Long teamId);
}
