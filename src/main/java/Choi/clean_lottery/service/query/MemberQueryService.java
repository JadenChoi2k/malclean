package Choi.clean_lottery.service.query;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private EntityManager em;

    public MemberDto findDto(Long memberId) {
        Member member = em.find(Member.class, memberId);
        if (member == null)
            return null;
        return new MemberDto(member);
    }
}
