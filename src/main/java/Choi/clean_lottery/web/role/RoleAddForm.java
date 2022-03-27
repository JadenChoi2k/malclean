package Choi.clean_lottery.web.role;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class RoleAddForm {
    @NotNull
    @Length(min = 1, max = 16)
    private String name;
    @NotEmpty
    @Size(min = 1)
    private List<String> areaNames;
    @NotEmpty
    @Size(min = 1)
    private List<Integer> difficulties;
    @NotEmpty
    @Size(min = 1)
    private List<Integer> minimumPeoples;
    @NotEmpty
    @Size(min = 1)
    private List<Boolean> changeable;
}
