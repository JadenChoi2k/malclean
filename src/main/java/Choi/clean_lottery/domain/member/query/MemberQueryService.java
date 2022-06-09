package Choi.clean_lottery.domain.member.query;

public interface MemberQueryService {

    MemberQueryInfo.WithTeam withTeam(Long memberId);
}
