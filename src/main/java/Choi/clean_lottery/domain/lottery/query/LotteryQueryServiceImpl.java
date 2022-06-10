package Choi.clean_lottery.domain.lottery.query;

import Choi.clean_lottery.application.lottery.LotteryFacade;
import Choi.clean_lottery.domain.lottery.LotteryReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryQueryServiceImpl implements LotteryQueryService {
    private final LotteryReader lotteryReader;

    @Override
    public LotteryQueryInfo retrieve(Long lotteryId) {
        return new LotteryQueryInfo(lotteryReader.getLotteryById(lotteryId));
    }
}
