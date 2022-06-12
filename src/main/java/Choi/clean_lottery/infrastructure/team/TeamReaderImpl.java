package Choi.clean_lottery.infrastructure.team;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TeamReaderImpl implements TeamReader {
    private final TeamJpaRepository teamRepository;

    @Override
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다. teamId: " + teamId));
    }
}
