package Choi.clean_lottery.domain.member;

public interface MemberService {
    MemberInfo registerMember(Long id, String name, String profileUrl);

    MemberInfo updateMemberProfile(Long id, String name, String profileUrl);

    void getOutOfTeam(Long memberId);

    void deleteMember(Long memberId);

    MemberInfo retrieveMemberInfo(Long memberId);
}
