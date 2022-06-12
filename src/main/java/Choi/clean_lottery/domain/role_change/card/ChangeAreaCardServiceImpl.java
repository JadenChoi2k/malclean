package Choi.clean_lottery.domain.role_change.card;

import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeAreaCardServiceImpl implements ChangeAreaCardService {
    private final ChangeAreaCardReader changeAreaCardReader;
    private final MemberReader memberReader;

    @Override
    @Transactional(readOnly = true)
    public ChangeRoleTableInfo.AreaCardInfo retrieve(Long cardId) {
        ChangeAreaCard card = changeAreaCardReader.getAreaCardById(cardId);
        return new ChangeRoleTableInfo.AreaCardInfo(card);
    }

    @Override
    @Transactional
    public void registerChanger(ChangeAreaCardCommand.RegisterChangerRequest registerChangerRequest) {
        ChangeAreaCard card = changeAreaCardReader.getAreaCardById(registerChangerRequest.getAreaCardId());
        card.setChanger(memberReader.getMemberById(registerChangerRequest.getMemberId()));
    }

    @Override
    @Transactional
    public void detachChanger(Long cardId) {
        ChangeAreaCard card = changeAreaCardReader.getAreaCardById(cardId);
        card.detachChanger();
    }

    @Override
    @Transactional
    public void completeChanging(Long cardId) {
        ChangeAreaCard card = changeAreaCardReader.getAreaCardById(cardId);
        card.completeChanging();
    }
}
