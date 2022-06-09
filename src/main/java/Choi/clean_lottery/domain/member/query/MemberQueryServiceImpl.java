package Choi.clean_lottery.domain.member.query;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.team.TeamInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {
    private final MemberReader memberReader;

    @Override
    @Transactional(readOnly = true)
    public MemberQueryInfo.WithTeam withTeam(Long memberId) {
        Member member = memberReader.getMemberById(memberId);
        return MemberQueryInfo.WithTeam.builder()
                .memberInfo(new MemberInfo(member))
                .teamInfo(member.getTeam() == null ? null : new TeamInfo(member.getTeam()))
                .build();
    }
}
