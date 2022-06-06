package Choi.clean_lottery.domain;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {
    private final DomainTestHelper testHelper = new DomainTestHelper();

    private Team getTeamWithRoles() {
        Team team = testHelper.createTeam();
        Role role1 = new Role(1L, "role1", team);
        role1.addArea(new Area(role1, "area11", 1, 0));
        role1.addArea(new Area(role1, "area12", 2, 0));
        role1.addArea(new Area(role1, "area13", 3, 0));
        role1.addArea(new Area(role1, "area14", 4, 2));
        role1.addArea(new Area(role1, "area15", 5, 2));
        Role role2 = new Role(2L, "role2", team);
        role2.addArea(new Area(role2, "area21", 1, 0));
        role2.addArea(new Area(role2, "area22", 2, 0));
        role2.addArea(new Area(role2, "area23", 3, 0));
        role2.addArea(new Area(role2, "area24", 4, 2));
        role2.addArea(new Area(role2, "area25", 5, 2));
        Role role3 = new Role(3L, "role3", team);
        role3.addArea(new Area(role3, "area31", 1, 0));
        role3.addArea(new Area(role3, "area32", 2, 0));
        role3.addArea(new Area(role3, "area33", 3, 0));
        role3.addArea(new Area(role3, "area34", 4, 2));
        role3.addArea(new Area(role3, "area35", 5, 2));
        team.addRole(role1);
        team.addRole(role2);
        team.addRole(role3);
        team.setCurrentRole(role1);
        return team;
    }

    @Test
    public void 현재_역할_받기() throws Exception {
        // given
        Team team = getTeamWithRoles();
        Role currentRole = team.getCurrentRole();
        List<Role> roles = team.getRoles();
        // when
        Role updateCurrentRole = team.updateCurrentRole(currentRole);
        // then
        assertEquals(currentRole, updateCurrentRole);
    }

    @Test
    public void 인수인계() throws Exception {
        // given
        // when
        // then
    }
}
