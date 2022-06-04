package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.area.Area;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LotteryCommand {

    @Getter
    @Setter
    @Builder
    public static class CreateLottery {
        private String name;
        private Team team;
        private Role role;
    }

    @Getter
    @Setter
    @Builder
    public static class DrawLottery {
        private List<Area> pick;
        private List<Member> participants;
    }
}
