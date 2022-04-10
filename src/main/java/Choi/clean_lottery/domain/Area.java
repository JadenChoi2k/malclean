package Choi.clean_lottery.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Area {
    @Id
    @GeneratedValue
    @Column(name = "area_id")
    private Long id;

    @JsonBackReference
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

    // 인수인계에서 해당하는 역할인지 여부.
    @Column
    @Setter
    private Boolean changeable;

    public Area(Role role, String name, Integer difficulty, Integer minimumPeople) {
        this(role, name, difficulty, minimumPeople, true);
    }

    public Area(Role role, String name, Integer difficulty, Integer minimumPeople, Boolean changeable) {
        this.role = role;
        this.name = name;
        this.difficulty = difficulty;
        this.minimumPeople = minimumPeople;
        this.changeable = changeable;
    }
}
