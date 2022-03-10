package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.LotteryResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LotteryResultDto {
    private MemberDto member;
    private AreaDto area;
    private LocalDateTime pickDate;

    public LotteryResultDto(LotteryResult lotteryResult) {
        member = new MemberDto(lotteryResult.getMember());
        area = new AreaDto(lotteryResult.getArea());
        pickDate = lotteryResult.getPickDate();
    }
}
