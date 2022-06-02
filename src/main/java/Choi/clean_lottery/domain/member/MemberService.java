package Choi.clean_lottery.domain.member;

public interface MemberService {
    MemberInfo registerMember(Long id, String name, String profileUrl);
    MemberInfo updateMemberProfile(Long id, String name, String profileUrl);
    MemberInfo retrieveMemberInfo(Long memberId);
}
