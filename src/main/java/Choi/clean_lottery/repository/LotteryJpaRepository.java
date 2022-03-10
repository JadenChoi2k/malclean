package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryJpaRepository extends JpaRepository<Lottery, Long> {
}
