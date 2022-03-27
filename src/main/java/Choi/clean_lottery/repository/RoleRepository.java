package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepository {

    private final EntityManager em;

    public void save(Role role) {
        em.persist(role);
    }

    public Role merge(Role role) {
        return em.merge(role);
    }

    public Role findById(Long roleId) {
        return em.find(Role.class, roleId);
    }

    public Role findWithAreas(Long roleId) {
        return em.createQuery(
                        "select r from Role r " +
                                "join fetch r.areas " +
                                "where r.id = :roleId", Role.class)
                .setParameter("roleId", roleId)
                .getSingleResult();
    }

    public List<Role> findRolesByTeam(Team team) {
        team = em.merge(team);
        return team.getRoles();
    }

    public List<Role> findRolesByTeamId(Long teamId) {
//        return em.createQuery("select r from Role r where r.team.id =:teamId")
//                .setParameter("teamId", teamId)
//                .getResultList();
        Team team = em.find(Team.class, teamId);
        return team.getRoles();
    }

    public void delete(Role role) {
        role = em.merge(role);
        if (role.getTeam().getCurrentRole() == role) {
            role.getTeam().setCurrentRole(null);
        }
        em.remove(role);
    }
}
