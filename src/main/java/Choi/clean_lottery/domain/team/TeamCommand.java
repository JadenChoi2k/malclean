package Choi.clean_lottery.domain.team;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class TeamCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterTeam {
        private String teamName;
        private Long managerId;
    }

    @Getter
    @Setter
    @Builder
    public static class AddMemberRequest {
        private Long teamId;
        private Long memberId;
    }

    /**
     * 팀의 역할들을 조회한 후, 역할의 순서를 주어진대로 바꾼다.
     */
    @Getter
    @Setter
    @Builder
    public static class ChangeCurrentRoleRequest {
        private Long teamId;
        private Long roleId;
        private LocalDate startDate;
    }

    @Getter
    @Setter
    @Builder
    public static class ChangeManagerRequest {
        private Long teamId;
        private Long managerId;
    }
}
