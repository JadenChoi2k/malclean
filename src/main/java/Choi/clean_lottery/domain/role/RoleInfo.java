package Choi.clean_lottery.domain.role;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
public class RoleInfo {
    private Long roleId;
    private String name;
    private List<Long> areaIdList;
    private LocalDate startDate;
}
