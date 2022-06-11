package Choi.clean_lottery.infrastructure.lottery;

import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.lottery.LotteryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotteryStoreImpl implements LotteryStore {
    private final LotteryJpaRepository lotteryRepository;

    @Override
    public Lottery store(Lottery lottery) {
        return lotteryRepository.save(lottery);
    }
}
