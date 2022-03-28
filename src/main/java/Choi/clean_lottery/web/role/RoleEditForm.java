package Choi.clean_lottery.web.role;

import Choi.clean_lottery.domain.Area;
import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.dto.RoleDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RoleEditForm {
    @NotNull
    private Long id;
    @NotNull
    @Length(min = 1, max = 16)
    private String name;

    public RoleEditForm() {}

    public RoleEditForm(Role role) {
        id = role.getId();
        name = role.getName();
    }

    public RoleEditForm(RoleDto role) {
        id = role.getId();
        name = role.getName();
    }
}
