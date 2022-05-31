package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.role.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private List<AreaDto> areas;
    private LocalDate startDate;

    public RoleDto(String name, LocalDate startDate, Integer duration) {
        this.name = name;
        this.startDate = startDate;
    }

    public RoleDto(Role role) {
        id = role.getId();
        name = role.getName();
        areas = role.getAreas().stream().map(AreaDto::new).collect(Collectors.toList());
        startDate = role.getStartDate();
    }

    public boolean isAreaOf(Long areaId) {
        if (areaId == null) {
            return false;
        }
        return areas.stream().filter(a -> a.getId().equals(areaId)).count() > 0;
    }
}
