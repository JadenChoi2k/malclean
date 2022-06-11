package Choi.clean_lottery.domain.lottery.query;

import Choi.clean_lottery.domain.lottery.LotteryReader;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LotteryQueryServiceImpl implements LotteryQueryService {
    private final LotteryReader lotteryReader;
    private final TeamReader teamReader;

    @Override
    public LotteryQueryInfo.Main retrieve(Long lotteryId) {
        return new LotteryQueryInfo.Main(lotteryReader.getLotteryById(lotteryId));
    }

    @Override
    public Page<LotteryQueryInfo.PageItem> retrievePage(Long teamId, Pageable pageable) {
        return lotteryReader.findAll(teamReader.getTeamById(teamId), pageable)
                .map(LotteryQueryInfo.PageItem::new);
    }
}
