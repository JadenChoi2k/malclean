package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.common.exception.IllegalStatusException;
import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.role.RoleReader;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeRoleTableServiceImpl implements ChangeRoleTableService {
    private final ChangeRoleTableStore changeRoleTableStore;
    private final ChangeRoleTableReader changeRoleTableReader;
    private final TeamReader teamReader;
    private final RoleReader roleReader;
    private final MemberReader memberReader;

    @Override
    @Transactional
    public ChangeRoleTableInfo.Main register(ChangeRoleTableCommand.RegisterTableRequest registerTableRequest) {
        var changeRoleTable = new ChangeRoleTable(
                teamReader.getTeamById(registerTableRequest.getTeamId()),
                roleReader.getRoleById(registerTableRequest.getReceiveRoleId()),
                roleReader.getRoleById(registerTableRequest.getGiveRoleId())
        );
        ChangeRoleTable stored = changeRoleTableStore.store(changeRoleTable);
        stored.startChanging();
        return new ChangeRoleTableInfo.Main(stored);
    }

    @Override
    @Transactional(readOnly = true)
    public ChangeRoleTableInfo.Main retrieve(Long tableId) {
        return new ChangeRoleTableInfo.Main(changeRoleTableReader.getTableById(tableId));
    }

    @Override
    @Transactional
    public void completeChanging(Long tableId) {
        ChangeRoleTable table = changeRoleTableReader.getTableById(tableId);
        if (!table.endChanging()) {
            throw new IllegalStatusException("인수인계가 아직 완료되지 않았습니다. tableId: " + tableId);
        }
    }
}
