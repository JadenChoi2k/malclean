package Choi.clean_lottery.domain.lottery;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.lottery.result.LotteryResult;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.ex.NotMemberOfTeam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lottery extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "lottery_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "lottery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LotteryResult> results = new ArrayList<>();

    public static Lottery createLottery(String name, Team team, Role role) {
        Lottery lottery = new Lottery();
        lottery.name = name;
        lottery.team = team;
        lottery.setRoleAndValidate(role);
        return lottery;
    }

    private boolean validateParticipants(List<Member> participants) throws NotMemberOfTeam {
        return participants.stream().allMatch(team::isMemberOf);
    }

    private void setRoleAndValidate(Role role) throws IllegalArgumentException {
        if (!team.isRoleOf(role))
            throw new IllegalArgumentException("팀에 존재하는 역할이 아닙니다.");
        this.role = role;
    }

    // 청소 역할 무작위로 뽑기
    public List<LotteryResult> drawLottery(List<Area> pick, List<Member> participants) throws IllegalArgumentException {
        // 검증 절차.
        if (!validatePicks(pick, participants)) throw new IllegalArgumentException("적절하지 않은 뽑기");
        if (!validateParticipants(participants)) throw new IllegalArgumentException("팀에 없는 멤버");
        // pick을 난이도에 따라 정렬. 어려운 거부터 줘야 하므로 내림차순 정렬
        pick.sort(Comparator.comparing(Area::getDifficulty).reversed());
        // 참가자들의 난이도 현황. 가장 낮은 사람부터 준다.
        PriorityQueue<Pair<Integer, Member>> difficultyPQ = new PriorityQueue<>(Comparator.comparing(Pair::getFirst));
        Random random = new Random();
        participants.forEach(participant -> difficultyPQ.offer(Pair.of(0, participant)));
        List<LotteryResult> result = new ArrayList<>();
        pick.forEach(area -> {
                    List<Pair<Integer, Member>> candidates = getListOfCurrentPickMember(difficultyPQ);
                    Pair<Integer, Member> picked = pickRandom(candidates, random);
                    Member pickedMember = picked.getSecond();
                    result.add(new LotteryResult(this, pickedMember, area));
                    candidates.forEach(pair -> {
                        if (pair.getSecond().equals(pickedMember)) {
                            difficultyPQ.offer(Pair.of(
                                    picked.getFirst() + area.getDifficulty(),
                                    pickedMember
                            ));
                        } else {
                            difficultyPQ.offer(pair);
                        }
                    });
                });
        this.results = result;
        return result;
    }

    // 난이도가 가장 낮은 멤버들을 반환.
    private List<Pair<Integer, Member>> getListOfCurrentPickMember(PriorityQueue<Pair<Integer, Member>> pq) {
        assert pq.peek() != null;
        int difficulty = pq.peek().getFirst();
        List<Pair<Integer, Member>> ret = new ArrayList<>();
        while (true) {
            if (pq.peek() == null) break;
            if (!(pq.peek().getFirst() == difficulty)) break;
            ret.add(pq.poll());
        }
        return ret;
    }

    private <T> T pickRandom(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    private boolean validatePicks(List<Area> pick, List<Member> participants) {
        if (pick.stream()
                .anyMatch(area -> area == null
                        || area.getId() == null
                        || !role.isAreaOf(area)))
            return false;
        Map<Long, Long> areaIdToCountMap = pick.stream()
                .map(Area::getId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (areaIdToCountMap
                .values().stream()
                .anyMatch(count -> count > participants.size()))
            return false;
        if (role.getAreas().stream()
                .anyMatch(area ->
                        Optional.ofNullable(areaIdToCountMap.get(area.getId()))
                                .map(aLong -> aLong < area.getMinimumPeople())
                                .orElseGet(() -> area.getMinimumPeople() > 0))
            )
            return false;
        return true;
    }

    public Map<Member, List<Area>> getParticipantsMap() {
        Map<Member, List<Area>> ret = new HashMap<>();
        results.forEach(lr -> {
                    if (!ret.containsKey(lr.getMember())) {
                        ret.put(lr.getMember(), new ArrayList<>());
                    }
                    ret.get(lr.getMember()).add(lr.getArea());
                });
        return ret;
    }
}
