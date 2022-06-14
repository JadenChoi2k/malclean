package Choi.clean_lottery.domain.member;

public interface MemberService {
    MemberInfo registerMember(MemberCommand.RegisterMemberRequest registerMemberRequest);

    MemberInfo updateMemberProfile(Long id, String name, String profileUrl);

    void changeManager(MemberCommand.ChangeManagerRequest changeManagerRequest);

    void getOutOfTeam(Long memberId);

    void deleteMember(Long memberId);

    MemberInfo retrieveMemberInfo(Long memberId);

    boolean exists(Long memberId);
}
