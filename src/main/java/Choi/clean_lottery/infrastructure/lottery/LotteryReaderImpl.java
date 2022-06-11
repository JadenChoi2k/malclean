package Choi.clean_lottery.infrastructure.lottery;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.lottery.LotteryReader;
import Choi.clean_lottery.domain.team.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotteryReaderImpl implements LotteryReader {
    private final LotteryJpaRepository lotteryJpaRepository;

    @Override
    public Lottery getLotteryById(Long lotteryId) {
        return lotteryJpaRepository.findById(lotteryId)
                .orElseThrow(() -> new EntityNotFoundException("청소 기록을 찾을 수 없습니다."));
    }

    @Override
    public Page<Lottery> findAll(Team team, Pageable pageable) {
        return lotteryJpaRepository.findAllByTeam(team, pageable);
    }
}
