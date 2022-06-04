package Choi.clean_lottery.domain;

import Choi.clean_lottery.domain.area.Area;
import Choi.clean_lottery.domain.lottery.Lottery;
import Choi.clean_lottery.domain.lottery.result.LotteryResult;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.ex.NotMemberOfTeam;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LotteryTest {

    private final DomainTestHelper testHelper = new DomainTestHelper();

    @Test
    public void 생성() throws Exception {
        // given
        List<Member> members = testHelper.createMembers();
        Team team = testHelper.createTeam();
        List<Area> areas = testHelper.createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        // when
        Lottery lottery = Lottery.createLottery("lottery", team, role);
        // then
        assertNotNull(lottery);
        assertEquals(lottery.getTeam().getId(), team.getId());
    }

    @Test
    public void 팀에_없는_역할_검증() throws Exception {
        // given
        List<Member> members = testHelper.createMembers();
        Team team = testHelper.createTeam();
        List<Area> areas = testHelper.createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        // team.addRole(role);
        members.forEach(m -> m.changeTeam(team));
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> Lottery.createLottery("lottery", team, role));
    }

    @Test
    public void 팀에_없는_멤버_검증() throws Exception {
        // given
        List<Member> members = testHelper.createMembers();
        Team team = testHelper.createTeam();
        List<Area> areas = testHelper.createAreas();
        Role role = new Role("role", team);
        areas.forEach(a -> role.addArea(a));
        team.addRole(role);
        // members.forEach(m -> m.changeTeam(team));
        // when
        // then
        assertThrows(NotMemberOfTeam.class, () -> Lottery.createLottery("lottery", team, role).drawLottery(team.getCurrentRole().getAreas(), members));
    }

    @Test
    public void 참가자_구역수_같음() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery();
        List<Member> members = lottery.getTeam().getMembers().subList(0, lottery.getRole().getAreas().size());
        System.out.println("lottery.getRole().areas = " + lottery.getRole().getAreas());
        // when
        List<LotteryResult> lotteryResults = lottery.drawLottery(lottery.getRole().getAreas(), members);
        List<Member> resultMembers = lotteryResults.stream().map(lr -> lr.getMember()).collect(Collectors.toList());
        lotteryResults.forEach(System.out::println);
        // then
        assertEquals(lotteryResults.size(), resultMembers.size());
        for (LotteryResult lotteryResult : lotteryResults) {
            assertEquals(resultMembers.stream().filter(lotteryResult.getMember()::equals).count(), 1);
        }
    }

    @Test
    public void 참가자수_더많음() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery();
        List<Area> pick = lottery.getRole().getAreas();
        List<Member> participants = lottery.getTeam().getMembers().subList(0, lottery.getRole().getAreas().size() + 1);
        // when
        List<LotteryResult> lotteryResults = lottery.drawLottery(pick, participants);
        List<Member> resultMembers = lotteryResults.stream().map(LotteryResult::getMember).collect(Collectors.toList());
        // then
        assertEquals(lotteryResults.size(), resultMembers.size());
        assertTrue(participants.size() > resultMembers.size());
        for (LotteryResult lotteryResult : lotteryResults) {
            assertEquals(resultMembers.stream().filter(lotteryResult.getMember()::equals).count(), 1);
        }
    }

    @Test
    public void 구역수_더많음() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery(5);
        List<Area> areas = lottery.getRole().getAreas();
        List<Member> participants = lottery.getTeam().getMembers().subList(0, areas.size() - 1);
        // when
        List<LotteryResult> lotteryResults = lottery.drawLottery(areas, participants);
//        for (LotteryResult lotteryResult : lotteryResults) {
//            System.out.print(lotteryResult.getMember().getName());
//            System.out.println(" -> " + lotteryResult.getArea().getName() + " " + lotteryResult.getArea().getDifficulty());
//        }
        Map<Member, List<Area>> resultMap = lottery.getParticipantsMap();
        // then
        int maxCount = Integer.MIN_VALUE;
        int minCount = Integer.MAX_VALUE;
        for (List<Area> resultAreas : resultMap.values()) {
            maxCount = Math.max(maxCount, resultAreas.size());
            minCount = Math.min(maxCount, resultAreas.size());
            for (Area resultArea : resultAreas) {
                assertEquals(1, resultAreas.stream().filter(resultArea::equals).count());
            }
        }
        assertTrue(maxCount - minCount <= 1);
    }

    @Test
    public void 너무_많은_구역_검증() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery(3);
        List<Member> participants = lottery.getTeam().getMembers().subList(0, 3);
        List<Area> areas = lottery.getRole().getAreas();
        List<Area> pick = new ArrayList<>();
        for (Area area : areas) {
            for (int i = 0; i < participants.size() + 1; i++) {
                pick.add(area);
            }
        }
        // then
        assertThrows(IllegalArgumentException.class, () -> lottery.drawLottery(pick, participants));
    }

    @Test
    public void 역할에_없는_구역_검증() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery(3);
        List<Area> pick = new ArrayList<>(List.copyOf(lottery.getRole().getAreas()));
        pick.add(new Area(100L, new Role(404L, "no_role", null), "no_area", 999, 0, true));
        // then
        assertThrows(IllegalArgumentException.class, () -> lottery.drawLottery(pick, lottery.getTeam().getMembers().subList(0, 3)));
    }

    @Test
    public void 최소인원보다_적은지_검증() throws Exception {
        // given
        Lottery lottery = testHelper.createLottery();
        List<Area> pick = new ArrayList<>();
        // when
        for (Area area : lottery.getRole().getAreas()) {
            if (area.getMinimumPeople() > 0)
                continue;
            pick.add(area);
        }
        // then
        assertThrows(IllegalArgumentException.class, () -> lottery.drawLottery(pick, lottery.getTeam().getMembers()));
    }
}