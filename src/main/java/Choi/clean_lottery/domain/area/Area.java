package Choi.clean_lottery.domain.area;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Area extends BaseTimeEntity {
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

    public Area(Long id, Role role, String name, Integer difficulty, Integer minimumPeople, Boolean changeable) {
        this(role, name, difficulty, minimumPeople, changeable);
        this.id = id;
    }

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

    public void changeAttribute(String name, Integer difficulty, Integer minimumPeople, Boolean changeable) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        if (difficulty != null && difficulty >= 0) {
            this.difficulty = difficulty;
        }
        if (minimumPeople != null && minimumPeople >= 0) {
            this.minimumPeople = minimumPeople;
        }
        if (changeable != null) {
            this.changeable = changeable;
        }
    }
}
