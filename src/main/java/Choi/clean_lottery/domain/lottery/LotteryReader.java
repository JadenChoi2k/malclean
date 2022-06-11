package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotteryReader {
    Lottery getLotteryById(Long lotteryId);

    Page<Lottery> findAll(Team team, Pageable pageable);
}
