package Choi.clean_lottery.infrastructure.invite;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.invite.Invite;
import Choi.clean_lottery.domain.invite.InviteReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InviteReaderImpl implements InviteReader {
    private final InviteJpaRepository inviteRepository;

    @Override
    public Invite getInviteById(String uuid) {
        return inviteRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("초대장을 찾을 수 없습니다. uuid: " + uuid));
    }
}
