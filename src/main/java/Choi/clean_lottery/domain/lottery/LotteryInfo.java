package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.lottery.result.LotteryResult;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LotteryInfo {

    @Getter
    public static class Main {
        private Long lotteryId;
        private String lotteryName;
        private Long roleId;
        private LocalDateTime createDate;
        private List<LotteryResultInfo> resultList;

        public Main(Lottery lottery) {
            this.lotteryId = lottery.getId();
            this.lotteryName = lottery.getName();
            this.roleId = lottery.getRole().getId();
            this.createDate = lottery.getCreateDate();
            this.resultList = lottery.getResults().stream()
                    .map(LotteryResultInfo::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class LotteryResultInfo {
        private Long resultId;
        private Long memberId;
        private Long areaId;

        public LotteryResultInfo(LotteryResult lotteryResult) {
            this.resultId = lotteryResult.getId();
            this.memberId = lotteryResult.getMember().getId();
            this.areaId = lotteryResult.getArea().getId();
        }
    }
}
