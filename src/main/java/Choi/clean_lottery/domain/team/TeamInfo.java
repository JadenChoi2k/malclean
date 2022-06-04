package Choi.clean_lottery.domain.team;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class TeamInfo {
    private Long teamId;
    private String teamName;
    private Long managerId;
    private List<Long> memberIdList;
    private Long currentRoleId;
    private List<Long> roleIdList;
    private Team.Status status;

    public TeamInfo(Team team) {
        this.teamId = team.getId();
        this.teamName = team.getName();
        this.managerId = team.getManager().getId();
        this.memberIdList = team.getMembers().stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        this.currentRoleId = team.getCurrentRole().getId();
        this.roleIdList = team.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        this.status = team.getState();
    }
}
