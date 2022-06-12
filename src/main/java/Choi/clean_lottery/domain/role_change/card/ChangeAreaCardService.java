package Choi.clean_lottery.domain.role_change.card;

import Choi.clean_lottery.domain.role_change.ChangeRoleTableCommand;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;

public interface ChangeAreaCardService {
    ChangeRoleTableInfo.AreaCardInfo retrieve(Long cardId);

    void registerChanger(ChangeAreaCardCommand.RegisterChangerRequest registerChangerRequest);

    void detachChanger(Long cardId);

    void completeChanging(Long cardId);
}
