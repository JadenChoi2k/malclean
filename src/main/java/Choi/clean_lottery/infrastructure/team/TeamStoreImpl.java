package Choi.clean_lottery.infrastructure.team;

import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.TeamStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamStoreImpl implements TeamStore {
    private final TeamJpaRepository teamRepository;

    @Override
    public Team store(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void delete(Long teamId) {
        teamRepository.deleteById(teamId);
    }
}
