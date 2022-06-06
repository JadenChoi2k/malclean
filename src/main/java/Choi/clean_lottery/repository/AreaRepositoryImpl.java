package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.role.area.Area;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
@RequiredArgsConstructor
public class AreaRepositoryImpl implements AreaRepositoryCustom {

    private final EntityManager em;

    @Override
    public void deleteAreaCascadeById(Long id) {
        em.createQuery("update LotteryResult set area = null where area.id = :areaId")
                .setParameter("areaId", id)
                .executeUpdate();
        em.createQuery("update ChangeAreaCard set area = null where area.id = :areaId")
                .setParameter("areaId", id)
                .executeUpdate();
        em.flush();
        em.clear();
        em.remove(em.find(Area.class, id));
    }
}
