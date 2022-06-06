package Choi.clean_lottery.domain.invite;

public interface InviteService {
    InviteInfo createInvite(InviteCommand.CreateInviteRequest createInviteRequest);

    InviteInfo acceptInvite(InviteCommand.AcceptInviteRequest acceptInviteRequest);

    InviteInfo rejectInvite(InviteCommand.RejectInviteRequest rejectInviteRequest);

    InviteInfo retrieveInvite(String uuid);
}
