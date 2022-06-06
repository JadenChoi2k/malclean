package Choi.clean_lottery.domain.lottery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LotteryCommand {

    @Getter
    @Setter
    @Builder
    public static class DrawLotteryRequest {
        private String lotteryName;
        private Long teamId;
        private Long roleId;
        private List<Long> pickAreaIdList;
        private List<Long> participantIdList;
    }
}
