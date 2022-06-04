package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.lottery.result.LotteryResult;

import java.util.List;

public interface LotteryStore {
    Lottery createLottery(LotteryCommand.CreateLottery createLot);

    List<LotteryResult> drawLottery(LotteryCommand.DrawLottery drawLot);
}
