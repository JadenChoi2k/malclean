package Choi.clean_lottery.infrastructure.lottery;

import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryJpaRepository extends JpaRepository<Lottery, Long> {
    // TODO: 팀 아이디로 받아오기.
    Page<Lottery> findAllByTeam(Team team, Pageable pageable);
}
