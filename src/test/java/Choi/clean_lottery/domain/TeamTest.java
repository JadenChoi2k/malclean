package Choi.clean_lottery.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
        role1.setNext(role2);
        role2.setNext(role3);
        role3.setNext(role1);
        return team;
    }

    @Test
    public void 순서대로_역할받기() throws Exception {
        // given
        Team team = testHelper.createTeam();
        Role role1 = new Role(1L, "role1", team);
        Role role2 = new Role(2L, "role2", team);
        Role role3 = new Role(3L, "role3", team);
        team.addRole(role1);
        team.addRole(role2);
        team.addRole(role3);
        team.setCurrentRole(role1);
        // when
        List<Role> rolesBySequence1 = team.getRolesBySequence();
        role1.setNext(role2);
        role2.setNext(role3);
        role3.setNext(role1);
        List<Role> rolesBySequence2 = team.getRolesBySequence();
        // then
        assertEquals(1, rolesBySequence1.size());
        assertEquals(3, team.getRoles().size());
        assertEquals(3, rolesBySequence2.size());
    }

    @Test
    public void 현재_역할_받기() throws Exception {
        // given
        Team team = getTeamWithRoles();
        Role currentRole = team.getCurrentRole();
        Role nextRole = currentRole.getNextRole();
        List<Role> rolesBySequence = team.getRolesBySequence();
        // when
        currentRole.setStartDate(LocalDate.now().minusDays(14));
        currentRole.setDuration(14);
        Role updateCurrentRole = team.updateCurrentRole();
        Role lastRole = team.getRolesBySequence().get(rolesBySequence.size() - 1);
        // then
        assertEquals(updateCurrentRole.getId(), nextRole.getId());
        assertEquals(lastRole.getId(), currentRole.getId());
    }

    @Test
    public void 인수인계() throws Exception {
        // given
        Team team = getTeamWithRoles();
        // when
        team.updateCurrentRole();
        // then
    }
}
