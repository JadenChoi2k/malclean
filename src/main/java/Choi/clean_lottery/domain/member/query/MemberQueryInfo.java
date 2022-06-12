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
        private MemberInfo memberInfo;
        private TeamInfo teamInfo;

        @Getter
        @Setter
        @Builder
        public static class TeamInfo {
            private Long teamId;
            private String name;
            private List<MemberInfo> members;
            private List<RoleInfo> roles;
            private RoleInfo currentRole;
            private Team.Status status;

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
                this.status = team.getState();
            }

            public boolean isRoleOf(Long roleId) {
                return roles.stream()
                        .anyMatch(r -> r.getRoleId().equals(roleId));
            }

            public boolean isChangingRole() {
                return this.status == Team.Status.CHANGING_ROLE;
            }
        }
    }
}
