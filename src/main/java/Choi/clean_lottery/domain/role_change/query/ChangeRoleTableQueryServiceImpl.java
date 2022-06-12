package Choi.clean_lottery.domain.role_change.query;

import Choi.clean_lottery.domain.role_change.ChangeRoleTableReader;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeRoleTableQueryServiceImpl implements ChangeRoleTableQueryService {
    private final ChangeRoleTableReader changeRoleTableReader;
    private final TeamReader teamReader;

    @Override
    public Long findIdByTeamId(Long teamId) {
        return changeRoleTableReader.getTableIdByTeam(teamReader.getTeamById(teamId));
    }
}
