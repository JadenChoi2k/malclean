package Choi.clean_lottery.domain.lottery;

public interface LotteryReader {
    Lottery findLotteryById(Long lotteryId);
}
