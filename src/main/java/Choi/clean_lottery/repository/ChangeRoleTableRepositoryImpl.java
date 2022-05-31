package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.Team.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
public class ChangeRoleTableRepositoryImpl implements ChangeRoleTableRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public ChangeRoleTable saveByTeamAndRole(Team team, Role receiveRole) {
        em.merge(team);
        ChangeRoleTable changeRoleTable = new ChangeRoleTable(team, receiveRole, team.getCurrentRole());
        team.setState(Team.Status.CHANGING_ROLE);
        em.persist(changeRoleTable);
        return changeRoleTable;
    }

    @Override
    public ChangeRoleTable saveByTeamAndRole(Long teamId, Long receiveRoleId) {
        Team team = em.find(Team.class, teamId);
        Role receiveRole = em.find(Role.class, receiveRoleId);
        return saveByTeamAndRole(team, receiveRole);
    }
}
