package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.invite.Invite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InviteRepository {

    private final EntityManager em;

    public void save(Invite invite) {
        em.persist(invite);
    }

    public Invite findById(String uuid) {
        return em.find(Invite.class, uuid);
    }

    public List<Invite> findAll() {
        return em.createQuery("select i from Invite i", Invite.class)
                .getResultList();
    }

    public boolean remove(String uuid) {
        Invite invite = findById(uuid);
        if (invite == null) return false;
        em.remove(invite);
        return true;
    }
}
