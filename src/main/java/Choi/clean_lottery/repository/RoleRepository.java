package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
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
        if (role.getTeam().getCurrentRole() == role) {
            role.getTeam().setCurrentRole(null);
        }
        role.getAreas().forEach(area -> area.setRole(null));
        em.createQuery("update Lottery set role = null" +
                " where role = :deleteRole")
                .setParameter("deleteRole", role)
                .executeUpdate();
        em.createQuery("select crt from ChangeRoleTable crt" +
                " where crt.giveRole = :role or crt.receiveRole = :role")
                .setParameter("role", role)
                .getResultStream()
                .forEach(em::remove);
        em.flush();
        em.clear();
        role = em.merge(role);
        em.remove(role);
    }
}
