package Choi.clean_lottery.domain.team;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.role.RoleReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamStore teamStore;
    private final TeamReader teamReader;
    private final MemberReader memberReader;
    private final RoleReader roleReader;

    @Override
    @Transactional
    public TeamInfo registerTeam(TeamCommand.RegisterTeam registerTeam) {
        Member manager = memberReader.getMemberById(registerTeam.getManagerId());
        Team team = teamStore.store(new Team(registerTeam.getTeamName(), manager));
        return new TeamInfo(team);
    }

    @Override
    @Transactional
    public TeamInfo addMemberToTeam(TeamCommand.AddMemberRequest addMemberRequest) {
        Member member = memberReader.getMemberById(addMemberRequest.getMemberId());
        Team team = teamReader.getTeamById(addMemberRequest.getTeamId());
        member.changeTeam(team);
        return new TeamInfo(team);
    }

    @Override
    @Transactional
    public void changeCurrentRole(TeamCommand.ChangeCurrentRoleRequest changeCurrentRoleRequest) {
        Team team = teamReader.getTeamById(changeCurrentRoleRequest.getTeamId());
        Role role = roleReader.getRoleById(changeCurrentRoleRequest.getRoleId());
        role.setStartDate(changeCurrentRoleRequest.getStartDate());
        team.setCurrentRole(role);
    }

    @Override
    @Transactional
    public TeamInfo changeManager(TeamCommand.ChangeManagerRequest changeManagerRequest) {
        Member newManager = memberReader.getMemberById(changeManagerRequest.getManagerId());
        Team team = teamReader.getTeamById(changeManagerRequest.getTeamId());
        team.setManager(newManager);
        return new TeamInfo(team);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamInfo retrieveTeamInfo(Long teamId) {
        Team team = teamReader.getTeamById(teamId);
        return new TeamInfo(team);
    }
}
