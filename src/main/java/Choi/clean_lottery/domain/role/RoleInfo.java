package Choi.clean_lottery.domain.role;

import Choi.clean_lottery.domain.role.area.AreaInfo;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class RoleInfo {
    private Long roleId;
    private String name;
    private List<AreaInfo> areaList;
    private LocalDate startDate;

    public RoleInfo(Role role) {
        this.roleId = role.getId();
        this.name = role.getName();
        this.areaList = role.getAreas().stream()
                .map(AreaInfo::new)
                .collect(Collectors.toList());
        this.startDate = role.getStartDate();
    }
}
