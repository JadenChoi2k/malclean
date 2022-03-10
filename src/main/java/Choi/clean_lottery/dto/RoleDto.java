package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.Role;
import Choi.clean_lottery.domain.RoleState;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private List<AreaDto> areas;
    private LocalDate startDate;
    private Integer duration;
    private RoleState state;

    public RoleDto(String name, LocalDate startDate, Integer duration) {
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
    }

    public RoleDto(Role role) {
        id = role.getId();
        name = role.getName();
        areas = role.getAreas().stream().map(AreaDto::new).collect(Collectors.toList());
        startDate = role.getStartDate();
        duration = role.getDuration();
        state = role.getRoleState(LocalDate.now());
    }

    public boolean isAreaOf(Long areaId) {
        if (areaId == null) {
            return false;
        }
        return areas.stream().filter(a -> a.getId().equals(areaId)).count() > 0;
    }
}
