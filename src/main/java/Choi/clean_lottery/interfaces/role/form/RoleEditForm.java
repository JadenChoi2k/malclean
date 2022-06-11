package Choi.clean_lottery.interfaces.role.form;

import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.dto.RoleDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
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
