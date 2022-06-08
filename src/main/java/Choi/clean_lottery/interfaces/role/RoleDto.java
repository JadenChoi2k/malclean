package Choi.clean_lottery.interfaces.role;

import Choi.clean_lottery.interfaces.role.area.AreaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RoleDto {

    @Getter
    @Setter
    @Builder
    public static class RegisterRoleRequest {
        @NotNull
        private Long teamId;
        @Length(min = 1, max = 16)
        private String name;
        @NotEmpty
        private List<AreaDto.RegisterAreaRequest> initAreaList;
    }

    @Getter
    @Setter
    @Builder
    public static class EditRoleRequest {
        private Long id;
        @Length(min = 1, max = 16)
        private String name;
    }
}
