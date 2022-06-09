package Choi.clean_lottery.domain.member.query;

import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.team.TeamInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberQueryInfo {

    @Getter
    @Setter
    @Builder
    public static class WithTeam {
        MemberInfo memberInfo;
        TeamInfo teamInfo;
    }
}
