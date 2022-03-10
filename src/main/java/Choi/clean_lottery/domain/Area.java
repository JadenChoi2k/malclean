package Choi.clean_lottery.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class Area {
    @Id
    @GeneratedValue
    @Column(name = "area_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @Setter
    private Role role;

    @Column(nullable = false, length = 20)
    @Setter
    private String name;

    // 청소의 난이도
    @Column(nullable = false)
    @Setter
    private Integer difficulty;

    // 최소 인원 수. 0 이상이어야 한다.
    @Column(nullable = false)
    @Setter
    private Integer minimumPeople;

    public Area() {
    }

    public Area(Role role, String name, Integer difficulty, Integer minimumPeople) {
        this.role = role;
        this.name = name;
        this.difficulty = difficulty;
        this.minimumPeople = minimumPeople;
    }
}
