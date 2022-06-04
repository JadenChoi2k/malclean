package Choi.clean_lottery.domain.lottery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryServiceImpl implements LotteryService {
    private final LotteryReader lotteryReader;
    private final LotteryStore lotteryStore;


}
