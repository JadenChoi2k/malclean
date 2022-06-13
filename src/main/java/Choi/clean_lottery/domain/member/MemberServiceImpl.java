package Choi.clean_lottery.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberReader memberReader;
    private final MemberStore memberStore;

    @Override
    @Transactional
    public MemberInfo registerMember(MemberCommand.RegisterMemberRequest registerMemberRequest) {
        if (memberReader.exists(registerMemberRequest.getId())) return null;
        log.info("member registered: [id={}] {} ({})",
                registerMemberRequest.getId(),
                registerMemberRequest.getName(),
                registerMemberRequest.getProfileUrl());
        return new MemberInfo(memberStore.store(registerMemberRequest.toEntity()));
    }

    @Override
    @Transactional
    public MemberInfo updateMemberProfile(Long id, String name, String profileUrl) {
        log.info("member update profile: [id={}] {} ({})", id, name, profileUrl);
        Member member = memberReader.getMemberById(id);
        member.updateProfile(name, profileUrl);
        return new MemberInfo(member);
    }



    @Override
    @Transactional
    public void getOutOfTeam(Long memberId) {
        log.info("[id={}] member get out of team", memberId);
        Member member = memberReader.getMemberById(memberId);
        member.getOutOfTeam();
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        log.info("[id={}] delete member!", memberId);
        memberStore.delete(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo retrieveMemberInfo(Long memberId) {
        return new MemberInfo(memberReader.getMemberById(memberId));
    }

    @Override
    public boolean exists(Long memberId) {
        return memberReader.exists(memberId);
    }
}
