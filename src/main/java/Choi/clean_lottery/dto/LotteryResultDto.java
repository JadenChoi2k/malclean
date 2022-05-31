package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.lottery.LotteryResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LotteryResultDto {
    private MemberDto member;
    private AreaDto area;
    private LocalDateTime pickDate;

    public LotteryResultDto(LotteryResult lotteryResult) {
        member = new MemberDto(lotteryResult.getMember());
        if (lotteryResult.getArea() != null) {
            area = new AreaDto(lotteryResult.getArea());
        } else {
            area = new AreaDto(-1L, "삭제됨", 0, 0, false);
        }
        pickDate = lotteryResult.getPickDate();
    }
}
