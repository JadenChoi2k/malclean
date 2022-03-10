package Choi.clean_lottery.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    // 팀의 매니저. 팀원들을 추가/삭제 할 수 있다. 청소 역할을 정할 수 있다.
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "manager_id")
    @Setter
    private Member manager;

    // 팀에 속하는 모든 인원은 청소 역할을 뽑을 수 있다.
    @OneToMany(mappedBy = "team", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    // 팀이 만들어진 날짜
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    // 현재 진행하고 있는 역할.
    // 인수인계 여부, 이전/다음 역할 등을 검증해야 한다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "current_role_id")
    @Setter
    private Role currentRole;

    // 팀이 갖고 있는 역할들. manager가 편집할 수 있다.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    // 팀이 여태 뽑았던 청소목록... 아마도 나중에는 필드가 없어지고 JPQL로 조회할 것이다.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Lottery> histories = new ArrayList<>();

    public Team(String name, Member manager) {
        this.name = name;
        this.manager = manager;
        manager.changeTeam(this);
        this.createDateTime = LocalDateTime.now();
    }

    public void addMember(Member member) {
        member.changeTeam(this);
    }

    public void kickOutMember(Member member) throws IllegalArgumentException {
        if (member.equals(manager)) {
            throw new IllegalArgumentException("매니저는 강퇴할 수 없습니다.");
        }
        members.remove(member);
        member.getOutOfTeam();
    }

    // 현재 역할부터 순서대로 역할을 받아온다.
    // 주의점... 역할이 currentRole을 시작점과 끝점으로 지정할 수 있는 닫힌 구간이 있어야 한다.
    // 무한 루프 방지를 위해 사이즈 제약을 걸었다. 더 나은 방법이 있기를 바란다. TODO
    public List<Role> getRolesBySequence() {
        // 만약 현재 역할이 없으면 roles 반환.
        if (currentRole == null) return getRoles();

        int maxSize = this.roles.size();
        Role nextRole = updateCurrentRole().getNextRole();
        List<Role> roles = new ArrayList<>();
        roles.add(currentRole);
        while (nextRole != null && !nextRole.getId().equals(currentRole.getId())) {
            roles.add(nextRole);
            nextRole = nextRole.getNextRole();
            if (roles.size() > maxSize) {
                System.out.println("아마도 무한루프?");
                break;
            }
        }
        return roles;
    }

    public Role updateCurrentRole() {
        RoleState roleState = currentRole.getRoleState(LocalDate.now());
        while (roleState == RoleState.ALREADY_CHANGED) {
            LocalDate changeDate = currentRole.getStartDate().plusDays(currentRole.getDuration());
            currentRole = currentRole.getNextRole();
            currentRole.setStartDate(changeDate);
            roleState = currentRole.getRoleState(LocalDate.now());
        }
        return currentRole;
    }

    public void addRole(Role role) {
        if (roles.contains(role)) {
            return;
        }
        role.setTeamRole(this);
    }

    // 이렇게 관계를 해제하고 나면 리포지토리에서 역할을 지워줘야 한다.
    public void subRole(Role role) {
        Role prev = role.getPrevRole();
        Role next = role.getNextRole();
        if (prev != null && next != null) {
            prev.setNext(next);
            next.setPrev(prev);
        }
        roles.remove(role);
    }

    public boolean isMemberOf(Member member) {
        return members.contains(member);
    }

    public boolean isRoleOf(Role role) {
        return roles.contains(role);
    }
}
