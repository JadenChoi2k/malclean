package Choi.clean_lottery.interfaces.role_changing;

import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.role.area.AreaInfo;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleChangingDtoMapper {

    ChangeRoleTableDto.Main of(ChangeRoleTableInfo.Main changeRoleTableInfo);

    ChangeRoleTableDto.AreaCardDto of(ChangeRoleTableInfo.AreaCardInfo areaCardInfo);

    ChangeRoleTableDto.AreaCardDto.AreaDto of(AreaInfo areaInfo);

    ChangeRoleTableDto.AreaCardDto.MemberDto of(MemberInfo memberInfo);
}
