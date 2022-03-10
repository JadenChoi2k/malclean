package Choi.clean_lottery.domain;

import Choi.clean_lottery.ex.NotMemberOfTeam;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
public class Lottery {
    @Id @GeneratedValue
    @Column(name = "lottery_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 단방향 ManyToMany
    @ManyToMany
    @JoinTable(name = "lottery_member",
            joinColumns = @JoinColumn(name = "lottery_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> participants = new ArrayList<>();

    // 팀에서 가져온다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime lastRoleDateTime;

    @OneToMany(mappedBy = "lottery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LotteryResult> results = new ArrayList<>();

    protected Lottery() {
    }

    public static Lottery createLottery(String name, Team team, List<Member> participants, Role role) {
        Lottery lottery = new Lottery();
        lottery.name = name;
        lottery.addTeamHistory(team);
        lottery.setParticipantsAndValidate(participants);
        lottery.setRoleAndValidate(role);
        return lottery;
    }

    private void addTeamHistory(Team team) {
        this.team = team;
        team.getHistories().add(this);
    }

    private void setParticipantsAndValidate(List<Member> participants) throws NotMemberOfTeam {
        for (Member participant : participants) {
            if (!team.isMemberOf(participant))
                throw new NotMemberOfTeam("팀의 멤버가 아닙니다");
        }
        this.participants = participants;
    }

    private void setRoleAndValidate(Role role) throws IllegalArgumentException {
        if (!team.isRoleOf(role))
            throw new IllegalArgumentException("팀에 존재하는 역할이 아닙니다.");
        this.role = role;
    }

    // 청소 역할 무작위로 뽑기
    public List<LotteryResult> drawLottery(List<Area> pick) throws IllegalArgumentException {
        // 검증 절차.
        boolean valid = validatePicks(pick);
        if (!valid) throw new IllegalArgumentException("적절하지 않은 뽑기");
        // 검증 끝. 랜덤 뽑기.
        // pick을 난이도에 따라 정렬. 어려운 거부터 줘야 하므로 내림차순 정렬
        pick.sort(Comparator.comparing(Area::getDifficulty).reversed());
        // 참가자들의 인덱스 목록. 어려울수록 앞 자리에 있다.
        int partinSize = participants.size();
        // key : 참가자 / value : 구역 목록.
        Map<Member, List<Area>> participantsMap = new HashMap<>();
        participants.forEach(m -> participantsMap.put(m, new ArrayList<>()));
        // 결과를 저장하는 리스트
        List<LotteryResult> _results = new ArrayList<>();

        Random random = new Random();
        lastRoleDateTime = LocalDateTime.now();
        for (int i = 0; i < pick.size(); i++) {
            // 만약 구역 수가 참가자 수를 넘어서기 시작하면.
            if (i >= partinSize) {
                int minCount = Integer.MAX_VALUE;
                int minDifficulty = Integer.MAX_VALUE;
                Member next = new Member();
                for (Map.Entry<Member, List<Area>> entry : participantsMap.entrySet()) {
                    int size = entry.getValue().size();
                    if (size < minCount && !entry.getValue().contains(pick.get(i))) {
                        next = entry.getKey();
                        minCount = size;
                    }
                    if (size == minCount && !entry.getValue().contains(pick.get(i))) {
                        int pickedMinDifficulty = entry.getValue().stream().map(Area::getDifficulty).mapToInt(a -> a).min().getAsInt();
                        if (pickedMinDifficulty < minDifficulty) {
                            next = entry.getKey();
                            minDifficulty = pickedMinDifficulty;
                        }
                    }
                }
                participantsMap.get(next).add(pick.get(i));
            } else {
                Member next = participants.get(random.nextInt(partinSize));
                while (!participantsMap.get(next).isEmpty()) {
                    next = participants.get(random.nextInt(partinSize));
                }
                participantsMap.get(next).add(pick.get(i));
            }
        }
        for (Map.Entry<Member, List<Area>> entry : participantsMap.entrySet()) {
            Member member = entry.getKey();
            List<Area> areas = entry.getValue();
            areas.forEach(a -> _results.add(new LotteryResult(this, member, a, lastRoleDateTime)));
        }
        this.results = _results;
        return results;
    }

    public List<LotteryResult> redrawLottery(List<Member> participants, Role role, List<Area> pick) throws IllegalArgumentException {
        setParticipantsAndValidate(participants);
        setRoleAndValidate(role);
        return drawLottery(pick);
    }

    private boolean validatePicks(List<Area> pick) {
        // pick이 과도하게 많으면 false 반환. O(N)
        Map<Area, Integer> pickCount = new HashMap<>();
        pick.forEach(p -> pickCount.put(p, 0));
        pick.forEach(p -> pickCount.put(p, pickCount.get(p) + 1));
        for (Integer count : pickCount.values()) {
            if (count > participants.size()) {
                return false;
            }
        }
        // pick 각자에 대해 검증
        for (Area area : pick) {
            if (area == null) {
                return false;
            }
            if (!role.isAreaOf(area)) {
                return false;
            }
        }
        // role의 area 각자에 대한 검증
        for (Area area : role.getAreas()) {
            // 최솟값보다 수가 적으면 false 반환.
            long count = pick.stream().filter(area::equals).count();
            if (count < area.getMinimumPeople()) {
                return false;
            }
        }
        return true;
    }

    public Map<Member, List<Area>> getParticipantsMap() {
        Map<Member, List<Area>> result = new HashMap<>();
        participants.forEach(m -> result.put(m, new ArrayList<>()));
        for (LotteryResult lotteryResult : results) {
            result.get(lotteryResult.getMember()).add(lotteryResult.getArea());
        }
        return result;
    }
}
