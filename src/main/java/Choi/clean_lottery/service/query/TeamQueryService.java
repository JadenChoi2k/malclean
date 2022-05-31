package Choi.clean_lottery.service.query;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final EntityManager em;

    public TeamDto findDto(Long teamId) {
        Team team = em.find(Team.class, teamId);
        return new TeamDto(team);
    }

    public TeamDto findDtoByMemberId(Long memberId) {
        Member member = em.find(Member.class, memberId);
        if (member == null || member.getTeam() == null)
            return null;
        return findDto(member.getTeam().getId());
    }
}
