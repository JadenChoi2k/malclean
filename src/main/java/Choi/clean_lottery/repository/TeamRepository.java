package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Member;
import Choi.clean_lottery.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private final EntityManager em;

    public void save(Team team) {
        em.persist(team);
    }

    public void merge(Team team) {
        em.merge(team);
    }

    public Team findOne(Long teamId) {
        return em.find(Team.class, teamId);
    }

    public List<Member> findMembers(Long teamId) {
        return em.createQuery("select m from Member m where m.team.id = :teamId", Member.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    public void delete(Team team) {
        team.getMembers().stream().forEach(m -> m.getOutOfTeam());
        team.setManager(null);
        em.createQuery("delete from Invite i where i.team = :team")
                .setParameter("team", team).executeUpdate();

//        em.createQuery("delete from Role r where r.team = :team")
//                        .setParameter("team", team).executeUpdate();
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class).
                getResultList();
    }
}
