package Choi.clean_lottery.interfaces.role.area;

import Choi.clean_lottery.domain.role.area.AreaCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AreaDtoMapper {

    AreaCommand.RegisterAreaRequest of(AreaDto.RegisterAreaRequest registerAreaRequest);

    @Mapping(source = "name", target = "areaName", defaultValue = "")
    @Mapping(target = "difficulty", defaultValue = "-1")
    @Mapping(target = "minimumPeople", defaultValue = "-1")
    AreaCommand.EditAreaRequest of(AreaDto.EditAreaRequest editAreaRequest);
}
