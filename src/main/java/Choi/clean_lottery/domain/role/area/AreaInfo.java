package Choi.clean_lottery.domain.role.area;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaInfo {
    private Long areaId;
    private String areaName;
    private Integer difficulty;
    private Integer minimumPeople;
    private Boolean changeable;

    public AreaInfo(Area area) {
        this.areaId = area.getId();
        this.areaName = area.getName();
        this.difficulty = area.getDifficulty();
        this.minimumPeople = area.getMinimumPeople();
        this.changeable = area.getChangeable();
    }
}
