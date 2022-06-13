package Choi.clean_lottery.infrastructure.role_change.card;

import Choi.clean_lottery.domain.role_change.card.ChangeAreaCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeAreaCardJpaRepository extends JpaRepository<ChangeAreaCard, Long> {
}
