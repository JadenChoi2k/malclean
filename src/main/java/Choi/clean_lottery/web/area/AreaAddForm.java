package Choi.clean_lottery.web.area;

import lombok.Data;

@Data
public class AreaAddForm {
    private Long roleId;
    private String name;
    private Integer difficulty;
    private Integer minimumPeople;
}
