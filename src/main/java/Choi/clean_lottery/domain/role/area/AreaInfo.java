package Choi.clean_lottery.domain.role.area;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaInfo {
    private Long id;
    private String name;
    private Integer difficulty;
    private Integer minimumPeople;
    private Boolean changeable;

    public AreaInfo(Area area) {
        this.id = area.getId();
        this.name = area.getName();
        this.difficulty = area.getDifficulty();
        this.minimumPeople = area.getMinimumPeople();
        this.changeable = area.getChangeable();
    }
}
