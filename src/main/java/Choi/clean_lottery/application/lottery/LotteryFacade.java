package Choi.clean_lottery.application.lottery;

import Choi.clean_lottery.domain.lottery.LotteryCommand;
import Choi.clean_lottery.domain.lottery.LotteryInfo;
import Choi.clean_lottery.domain.lottery.LotteryService;
import Choi.clean_lottery.domain.lottery.query.LotteryQueryInfo;
import Choi.clean_lottery.domain.lottery.query.LotteryQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryFacade {
    private final LotteryService lotteryService;
    private final LotteryQueryService lotteryQueryService;

    public LotteryInfo.Main retrieve(Long lotteryId) {
        return lotteryService.retrieve(lotteryId);
    }

    public LotteryInfo.Main drawLottery(LotteryCommand.DrawLotteryRequest drawLotteryRequest) {
        return lotteryService.drawLottery(drawLotteryRequest);
    }

    public LotteryQueryInfo.Main retrieveQuery(Long lotteryId) {
        return lotteryQueryService.retrieve(lotteryId);
    }

    public Page<LotteryQueryInfo.PageItem> retrievePageQuery(Long teamId, Pageable pageable) {
        return lotteryQueryService.retrievePage(teamId, pageable);
    }
}
