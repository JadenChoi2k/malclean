package Choi.clean_lottery.infrastructure.invite;

import Choi.clean_lottery.domain.invite.Invite;
import Choi.clean_lottery.domain.invite.InviteStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InviteStoreImpl implements InviteStore {
    private final InviteJpaRepository inviteRepository;

    @Override
    public Invite store(Invite invite) {
        return inviteRepository.save(invite);
    }
}
