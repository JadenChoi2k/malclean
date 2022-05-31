package Choi.clean_lottery.service.query;

import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.dto.LotteryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LotteryQueryService {

    private final EntityManager em;

    public LotteryDto findDto(Long lotteryId) {
        Lottery lottery = em.find(Lottery.class, lotteryId);
        return new LotteryDto(lottery);
    }

    public List<LotteryDto> findAllDtoInTeam(Long teamId, int page, int count) {
        return em.createQuery(
                "select new Choi.clean_lottery.dto.LotteryDto(l)" +
                        " from Lottery l" +
                        " left join l.role r" +
                        " where l.team.id = :teamId" +
                        " order by l.lastRoleDateTime desc", LotteryDto.class)
                .setParameter("teamId", teamId)
                .setFirstResult(page * count)
                .setMaxResults(count)
                .getResultList();
    }
}
