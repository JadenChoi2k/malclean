package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.Area;
import lombok.Data;

@Data
public class AreaDto {
    private Long id;
    private String name;
    private Integer difficulty;
    private Integer minimumPeople;

    public AreaDto(Area area) {
        id = area.getId();
        name = area.getName();
        difficulty = area.getDifficulty();
        minimumPeople = area.getMinimumPeople();
    }
}
