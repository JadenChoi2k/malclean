package Choi.clean_lottery.web.role;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Data
public class RoleAddForm {
    @NotNull
    @Length(min = 1, max = 16)
    private String name;
    @NotEmpty
    @Size(min = 1, max = 10)
    private List<@Pattern(regexp = "[a-z가-힣0-9\\- ]{1,15}") String> areaNames;
    @NotEmpty
    @Size(min = 1, max = 10)
    private List<@Max(value = 10) Integer> difficulties;
    @NotEmpty
    @Size(min = 1, max = 10)
    private List<@Max(value = 10) Integer> minimumPeoples;
    @NotEmpty
    @Size(min = 1, max = 10)
    private List<Boolean> changeable;
}
