package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.TeamState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamDto {
    private Long id;
    private String name;
    private MemberDto manager;
    private List<MemberDto> members;
    private LocalDateTime createDateTime;
    private RoleDto currentRole;
    private List<RoleDto> roles;
    private TeamState state;

    public TeamDto(Team team) {
        id = team.getId();
        name = team.getName();
        manager = new MemberDto(team.getManager());
        members = team.getMembers().stream().map(MemberDto::new).collect(Collectors.toList());
        createDateTime = team.getCreateDateTime();
        if (team.getCurrentRole() != null) {
            currentRole = new RoleDto(team.getCurrentRole());
        }
        roles = team.getRoles().stream().map(RoleDto::new).collect(Collectors.toList());
        state = team.getState();
    }

    public boolean isRoleOf(Long roleId) {
        return roles.stream().filter(r -> r.getId().equals(roleId)).count() > 0;
    }
}
