package Choi.clean_lottery.interfaces.invite;

import Choi.clean_lottery.domain.invite.InviteInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface InviteDtoMapper {
    InviteDto of(InviteInfo inviteInfo);
}
