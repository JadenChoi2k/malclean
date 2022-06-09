package Choi.clean_lottery.application.invite;

import Choi.clean_lottery.domain.invite.InviteCommand;
import Choi.clean_lottery.domain.invite.InviteInfo;
import Choi.clean_lottery.domain.invite.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteFacade {
    private final InviteService inviteService;

    public InviteInfo createInvite(InviteCommand.CreateInviteRequest createInviteRequest) {
        return inviteService.createInvite(createInviteRequest);
    }

    public InviteInfo acceptInvite(InviteCommand.AcceptInviteRequest acceptInviteRequest) {
        return inviteService.acceptInvite(acceptInviteRequest);
    }

    public InviteInfo rejectInvite(InviteCommand.RejectInviteRequest rejectInviteRequest) {
        return inviteService.rejectInvite(rejectInviteRequest);
    }

    public InviteInfo retrieveInvite(String uuid) {
        return inviteService.retrieveInvite(uuid);
    }
}
