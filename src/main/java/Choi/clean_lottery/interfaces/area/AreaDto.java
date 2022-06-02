package Choi.clean_lottery.interfaces.area;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AreaDto {

    @Getter
    @Setter
    @Builder
    public static class AreaInfo {
        private Long id;
        private String name;
        private Integer difficulty;
        private Integer minimumPeople;
        private Boolean changeable;
    }

    @Getter
    @Setter
    @Builder
    public static class RegisterAreaRequest {
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

    @Getter
    @Setter
    @Builder
    public static class ChangeAreaRequest {
        @NotNull
        private Long roleId;
        @NotNull
        private Long areaId;
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
}
