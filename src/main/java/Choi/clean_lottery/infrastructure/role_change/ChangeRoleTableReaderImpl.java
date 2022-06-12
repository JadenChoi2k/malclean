package Choi.clean_lottery.infrastructure.role_change;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableReader;
import Choi.clean_lottery.domain.team.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeRoleTableReaderImpl implements ChangeRoleTableReader {
    private final ChangeRoleTableJpaRepository tableRepository;

    @Override
    public ChangeRoleTable getTableById(Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("인수인계 테이블을 찾을 수 없습니다. tableId: " + tableId));
    }

    @Override
    public Long getTableIdByTeam(Team team) {
        return tableRepository.findFirstByTeamOrderByCreateDateDesc(team)
                .orElseThrow(() -> new EntityNotFoundException("인수인계 중이 아닙니다. teamId: " + team.getId()))
                .getId();
    }
}
