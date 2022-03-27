package Choi.clean_lottery.repository;

import Choi.clean_lottery.domain.ChangeRoleTable;
import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.Team;
import Choi.clean_lottery.domain.TeamState;
import Choi.clean_lottery.dto.ChangeRoleTableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
        team.setState(TeamState.CHANGING_ROLE);
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
