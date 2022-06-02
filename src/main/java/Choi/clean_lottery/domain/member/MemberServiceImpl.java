package Choi.clean_lottery.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberReader memberReader;
    private final MemberStore memberStore;

    @Override
    public MemberInfo registerMember(Long id, String name, String profileUrl) {
        return new MemberInfo(memberStore.store(new Member(id, name, profileUrl)));
    }

    @Override
    public MemberInfo updateMemberProfile(Long id, String name, String profileUrl) {
        Member member = memberReader.getMemberById(id);
        member.updateProfile(name, profileUrl);
        return new MemberInfo(member);
    }

    @Override
    public MemberInfo retrieveMemberInfo(Long memberId) {
        return new MemberInfo(memberReader.getMemberById(memberId));
    }
}
