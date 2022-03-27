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
public class Role extends BaseTimeEntity {
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

    public Role(Long id, String name, Team team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public Role(String name, Team team) {
        this.setName(name);
        this.team = team;
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
}
