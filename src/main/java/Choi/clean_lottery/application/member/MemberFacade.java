package Choi.clean_lottery.application.member;

import Choi.clean_lottery.domain.member.MemberCommand;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;

    public MemberInfo register(MemberCommand.RegisterMemberRequest registerMemberRequest) {
        return memberService.registerMember(registerMemberRequest);
    }

    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }

    public void getOutOfTeam(Long memberId) {
        memberService.getOutOfTeam(memberId);
    }

    public MemberInfo retrieveMemberInfo(Long memberId) {
        return memberService.retrieveMemberInfo(memberId);
    }
}
