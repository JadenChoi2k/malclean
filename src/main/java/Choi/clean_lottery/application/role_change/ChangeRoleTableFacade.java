package Choi.clean_lottery.application.role_change;

import Choi.clean_lottery.domain.role_change.ChangeRoleTableCommand;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableService;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCardCommand;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCardService;
import Choi.clean_lottery.domain.role_change.query.ChangeRoleTableQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeRoleTableFacade {
    private final ChangeRoleTableService changeRoleTableService;
    private final ChangeRoleTableQueryService changeRoleTableQueryService;
    private final ChangeAreaCardService changeAreaCardService;

    public ChangeRoleTableInfo.Main register(ChangeRoleTableCommand.RegisterTableRequest registerTableRequest) {
        return changeRoleTableService.register(registerTableRequest);
    }

    public ChangeRoleTableInfo.Main retrieve(Long tableId) {
        return changeRoleTableService.retrieve(tableId);
    }

    public void completeChanging(Long tableId) {
        changeRoleTableService.completeChanging(tableId);
    }

    /* changeRoleTableQueryService */
    public Long findTableIdByTeamId(Long teamId) {
        return changeRoleTableQueryService.findIdByTeamId(teamId);
    }

    /* ChangeAreaCardService */
    public ChangeRoleTableInfo.AreaCardInfo areaCardRetrieve(Long cardId) {
        return changeAreaCardService.retrieve(cardId);
    }

    public void areaCardRegisterChanger(ChangeAreaCardCommand.RegisterChangerRequest registerChangerRequest) {
        changeAreaCardService.registerChanger(registerChangerRequest);
    }

    public void areaCardDetachChanger(Long cardId) {
        changeAreaCardService.detachChanger(cardId);
    }

    public void areaCardCompleteChanging(Long cardId) {
        changeAreaCardService.completeChanging(cardId);
    }
}
