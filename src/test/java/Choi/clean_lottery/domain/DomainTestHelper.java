package Choi.clean_lottery.domain;

import Choi.clean_lottery.domain.area.Area;
import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;

import java.util.ArrayList;
import java.util.List;

public class DomainTestHelper {
    public List<Member> createMembers() {
        List<Member> members = new ArrayList<>();
        for(long i = 0; i < 10L; i++) {
            members.add(new Member(i, "member" + i, null));
        }
        return members;
    }

    public Team createTeam() {
        Member manager = new Member(1111L, "manager", null);
        Team team = new Team("team", manager);
        return team;
    }

    public Team createTeamWithMembers() {
        Team team = createTeam();
        createMembers().forEach(team::addMember);
        return team;
    }

    public List<Area> createAreas() {
        return createAreas("area");
    }

    public List<Area> createAreas(String prefix) {
        List<Area> areas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Area area = new Area((long) i, null, prefix + i, i, 1, true);
            areas.add(area);
        }
        for (int i = 3; i < 6; i++) {
            Area area = new Area((long) i, null, prefix + i, i, 0, false);
            areas.add(area);
        }
        return areas;
    }

    public Role createRole(List<Area> areas, Long id, String name) {
        Role role = new Role(id, name, null);
        role.addAllArea(areas);
        return role;
    }

    public Lottery createLottery() {
        List<Member> members = createMembers();
        Team team = createTeam();
        List<Area> areas = createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        Lottery lottery = Lottery.createLottery("lottery", team, role);
        return lottery;
    }

    public Lottery createLottery(int participantSize) {
        List<Member> members = createMembers();
        Team team = createTeam();
        List<Area> areas = createAreas();
        Role role = new Role("role", team);
        areas.forEach(role::addArea);
        team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        return Lottery.createLottery("lottery", team, role);
    }
}
