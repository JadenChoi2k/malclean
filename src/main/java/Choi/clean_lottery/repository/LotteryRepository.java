package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Lottery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LotteryRepository {

    private final EntityManager em;

    public void save(Lottery lottery) {
        em.persist(lottery);
    }

    public Lottery findById(Long id) {
        return em.find(Lottery.class, id);
    }

    public List<Lottery> findAllInTeam(Long teamId) {
        return em.createQuery("select lot from Lottery lot where lot.team.id =:teamId")
                .setParameter("teamId", teamId)
                .getResultList();
    }

    public List<Lottery> pagingInTeam(Long teamId, int page, int count) {
        return em.createQuery("select l from Lottery l where l.team.id=:teamId order by l.lastRoleDateTime desc")
                .setParameter("teamId", teamId)
                .setFirstResult(page * count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long getTotalSizeInTeam(Long teamId) {
        return (Long) em.createQuery("select count(l) from Lottery l where l.team.id=:teamId")
                .setParameter("teamId", teamId)
                .getSingleResult();
    }
}
