package Choi.clean_lottery.web.area;

import lombok.Data;

@Data
public class AreaEditForm {
    private Long roleId;
    private Long areaId;
    private String name;
    private Integer difficulty;
    private Integer minimumPeople;
    private Boolean changeable;
}
