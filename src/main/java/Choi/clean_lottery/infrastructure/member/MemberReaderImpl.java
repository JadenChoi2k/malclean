package Choi.clean_lottery.infrastructure.member;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberReaderImpl implements MemberReader {
    private final MemberJpaRepository memberRepository;

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. memberId: " + memberId));
    }

    @Override
    public boolean exists(Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
