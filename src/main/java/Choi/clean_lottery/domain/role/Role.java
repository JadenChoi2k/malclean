package Choi.clean_lottery.domain.role;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.team.Team;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
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

    public void changeName(String name) {
        if (name == null) throw new IllegalArgumentException("적절하지 않은 요청입니다.");
        this.name = name;
    }

    public boolean isAreaOf(Area area) {
        return areas.contains(area);
    }

    public void addArea(Area area) {
        areas.add(area);
        area.setRole(this);
    }

    public void addAllArea(Collection<Area> addAreas) {
        areas.addAll(addAreas);
        addAreas.forEach(a -> a.setRole(this));
    }

    public void subArea(Area area) {
        areas.remove(area);
        area.setRole(null);
    }

    public void detachFromTeam() {
        this.team = null;
    }

    public void setTeamRole(Team team) {
        this.team = team;
        if (team == null) {
            return;
        }
        if (!team.getRoles().contains(this)) {
            team.getRoles().add(this);
        }
    }
}
