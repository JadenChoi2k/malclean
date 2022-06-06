package Choi.clean_lottery.domain.lottery;

public interface LotteryService {

    LotteryInfo.Main getLotteryInfo(Long lotteryId);

    LotteryInfo.Main drawLottery(LotteryCommand.DrawLotteryRequest drawLotteryRequest);
}
