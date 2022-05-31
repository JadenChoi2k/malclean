package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.area.Area;
import lombok.Data;

@Data
public class AreaDto {
    private Long id;
    private String name;
    private Integer difficulty;
    private Integer minimumPeople;
    private Boolean changeable;

    public AreaDto(Area area) {
        id = area.getId();
        name = area.getName();
        difficulty = area.getDifficulty();
        minimumPeople = area.getMinimumPeople();
        changeable = area.getChangeable();
    }

    public AreaDto(Long id, String name, Integer difficulty, Integer minimumPeople, Boolean changeable) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.minimumPeople = minimumPeople;
        this.changeable = changeable;
    }
}
