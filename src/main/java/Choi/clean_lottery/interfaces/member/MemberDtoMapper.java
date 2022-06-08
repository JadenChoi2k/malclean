package Choi.clean_lottery.interfaces.member;

import Choi.clean_lottery.domain.member.MemberCommand;
import Choi.clean_lottery.interfaces.social.SocialUserInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface MemberDtoMapper {
    @Mapping(source = "socialId", target = "id")
    @Mapping(source = "nickname", target = "name")
    @Mapping(source = "profileImageUrl", target = "profileUrl")
    MemberCommand.RegisterMemberRequest of(SocialUserInfo socialUserInfo);
}
