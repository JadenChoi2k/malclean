package Choi.clean_lottery.infrastructure.role_change.card;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCard;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCardReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeAreaCardReaderImpl implements ChangeAreaCardReader {
    private final ChangeAreaCardJpaRepository changeAreaCardRepository;

    @Override
    public ChangeAreaCard getAreaCardById(Long cardId) {
        return changeAreaCardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("구역 카드를 찾을 수 없습니다. cardId: " + cardId));
    }
}
