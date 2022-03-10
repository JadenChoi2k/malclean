package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Area;
import Choi.clean_lottery.domain.LotteryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class AreaLotteryResultRepository {

    private final EntityManager em;

    public void deleteById(Long areaId) {
        List<LotteryResult> lotteryResults = em.createQuery("select lr from LotteryResult lr " +
                        " where lr.area.id = :areaId", LotteryResult.class)
                .setParameter("areaId", areaId)
                .getResultList();
        lotteryResults.forEach(lr -> lr.setArea(null));
        em.flush();
        em.clear();
        em.remove(em.find(Area.class, areaId));
    }
}
