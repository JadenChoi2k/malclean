package Choi.clean_lottery.domain.member.query;

import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.role.RoleInfo;
import Choi.clean_lottery.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class MemberQueryInfo {

    @Getter
    @Setter
    @Builder
    public static class WithTeam {
        MemberInfo memberInfo;
        TeamInfo teamInfo;

        @Getter
        @Setter
        @Builder
        public static class TeamInfo {
            private Long teamId;
            private String name;
            List<MemberInfo> members;
            List<RoleInfo> roles;
            RoleInfo currentRole;

            public TeamInfo(Team team) {
                this.teamId = team.getId();
                this.name = team.getName();
                this.members = team.getMembers().stream()
                        .map(MemberInfo::new)
                        .collect(Collectors.toList());
                this.roles = team.getRoles().stream()
                        .map(RoleInfo::new)
                        .collect(Collectors.toList());
                this.currentRole = new RoleInfo(team.getCurrentRole());
            }
        }
    }
}
