package Choi.clean_lottery.web.area;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AreaAddForm {
    @NotNull
    private Long roleId;
    @Length(min = 2, max = 15)
    private String name;
    @Max(10)
    @Min(0)
    private Integer difficulty;
    @Max(10)
    @Min(0)
    private Integer minimumPeople;
    private Boolean changeable;
}
