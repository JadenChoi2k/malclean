package Choi.clean_lottery.domain.role.area;

import Choi.clean_lottery.domain.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AreaCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterAreaRequest {
        private Long roleId;
        private String areaName;
        private Integer difficulty;
        private Integer minimumPeople;
        private Boolean changeable;

        public Area toEntity(Role role) {
            return new Area(
                    role,
                    areaName,
                    difficulty,
                    minimumPeople,
                    changeable
            );
        }
    }

    @Getter
    @Setter
    @Builder
    public static class EditAreaRequest {
        private Long areaId;
        private String areaName;
        private Integer difficulty;
        private Integer minimumPeople;
        private Boolean changeable;
    }
}
