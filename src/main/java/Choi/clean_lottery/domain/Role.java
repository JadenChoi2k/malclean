package Choi.clean_lottery.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false, length = 20)
    @Setter
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Area> areas = new ArrayList<>();

    // 시작 날짜
    @Setter
    private LocalDate startDate;
    // 지속 기간
    @Setter
    private Integer duration;
    // 이전/다음 청소 역할. 연결 리스트처럼 되어 있는 것이다.
    @OneToOne
    @JoinColumn(name = "prev_role_id")
    private Role prevRole;
    @OneToOne
    @JoinColumn(name = "next_role_id")
    private Role nextRole;

    public Role(Long id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public Role(String name, Team team) {
        this.setName(name);
        this.team = team;
    }

    // 오늘 날짜를 건네주면 오늘이 인수인계 날인지, 이미 바뀌어야 하는 상태인지 등을 알려줌
    public RoleState getRoleState(LocalDate date) {
        // 만약 startDate 또는 duration이 null이라면 그냥 가능하다.
        if (startDate == null || duration == null) {
            return RoleState.ON;
        }
        long elapsed_day = ChronoUnit.DAYS.between(startDate, date);

        if (elapsed_day < duration - 1)
            return RoleState.ON;
        else if (elapsed_day == duration - 1)
            return RoleState.CHANGE_DAY;
        else
            return RoleState.ALREADY_CHANGED;
    }

    public boolean isAreaOf(Area area) {
        return areas.contains(area);
    }

    public void addArea(Area area) {
        areas.add(area) ;
    }

    public void addAllArea(Collection<Area> addAreas) {
        areas.addAll(addAreas);
    }

    public void subArea(Area area) {
        areas.remove(area);
    }

    public void shrinkArea(int newSize) {
        areas = areas.subList(0, newSize);
    }

    public void setTeamRole(Team team) {
        this.team = team;
        team.getRoles().add(this);
    }

    public void setPrev(Role role) {
        this.prevRole = role;
    }

    public void setNext(Role role) {
        this.nextRole = role;
    }
}
