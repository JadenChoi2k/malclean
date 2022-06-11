package Choi.clean_lottery.domain.lottery;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LotteryService {

    LotteryInfo.Main retrieve(Long lotteryId);

    LotteryInfo.Main drawLottery(LotteryCommand.DrawLotteryRequest drawLotteryRequest);
}
