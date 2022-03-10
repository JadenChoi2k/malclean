package Choi.clean_lottery.domain;

import java.util.ArrayList;
import java.util.List;

public class DomainTestHelper {
    public List<Member> createMembers() {
        List<Member> members = new ArrayList<>();
        for(long i = 0; i < 10L; i++) {
            members.add(new Member(i, "member" + i, null, null, null));
        }
        return members;
    }

    public Team createTeam() {
        Member manager = new Member(1111L, "manager", null, null, null);
        Team team = new Team("team", manager);
        return team;
    }

    public List<Area> createAreas() {
        List<Area> areas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Area area = new Area(null, "area" + i, i, 1);
            areas.add(area);
        }
        for (int i = 3; i < 6; i++) {
            Area area = new Area(null, "area" + i, i, 0);
            areas.add(area);
        }
        return areas;
    }

    public Lottery createLottery() {
        List<Member> members = createMembers();
        Team team = createTeam();
        List<Area> areas = createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        Lottery lottery = Lottery.createLottery("lottery", team, team.getMembers(), role);
        return lottery;
    }

    public Lottery createLottery(int partcipantSize) {
        List<Member> members = createMembers();
        Team team = createTeam();
        List<Area> areas = createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        Lottery lottery = Lottery.createLottery("lottery", team, team.getMembers().subList(0, partcipantSize), role);
        return lottery;
    }
}
