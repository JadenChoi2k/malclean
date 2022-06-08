package Choi.clean_lottery.interfaces.role;

import Choi.clean_lottery.domain.role.RoleCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleDtoMapper {

    @Mapping(source = "initAreaList", target = "areaRequestList")
    RoleCommand.RegisterRoleRequest of(RoleDto.RegisterRoleRequest registerRoleRequest);

    @Mapping(source="id", target = "id", defaultValue = "-1L")
    RoleCommand.EditRoleRequest of(RoleDto.EditRoleRequest editRoleRequest);
}
