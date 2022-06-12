package Choi.clean_lottery.infrastructure.member;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberStoreImpl implements MemberStore {
    private final MemberJpaRepository memberRepository;

    @Override
    public Member store(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
