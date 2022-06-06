package Choi.clean_lottery.domain.invite;

import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.member.MemberReader;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteServiceImpl implements InviteService {
    private final InviteStore inviteStore;
    private final InviteReader inviteReader;
    private final MemberReader memberReader;
    private final TeamReader teamReader;

    @Override
    public InviteInfo createInvite(InviteCommand.CreateInviteRequest createInviteRequest) {
        Invite invite = Invite.create(
                memberReader.getMemberById(createInviteRequest.getSenderId()),
                teamReader.getTeamById(createInviteRequest.getTeamId())
        );
        inviteStore.store(invite);
        return new InviteInfo(invite);
    }

    @Override
    public InviteInfo acceptInvite(InviteCommand.AcceptInviteRequest acceptInviteRequest) {
        Invite invite = inviteReader.getInviteById(acceptInviteRequest.getUuid());
        if (invite == null) throw new IllegalArgumentException("존재하지 않는 초대장입니다.");
        Member receiver = memberReader.getMemberById(acceptInviteRequest.getReceiverId());
        if (receiver == null) throw new IllegalArgumentException("존재하지 않는 멤버입니다.");
        boolean result = invite.accept(receiver);
        if (!result) throw new IllegalArgumentException("처리되었거나 거절된 초대장입니다.");
        invite.accept(receiver);
        return new InviteInfo(invite);
    }

    @Override
    public InviteInfo rejectInvite(InviteCommand.RejectInviteRequest rejectInviteRequest) {
        Invite invite = inviteReader.getInviteById(rejectInviteRequest.getUuid());
        if (invite == null) throw new IllegalArgumentException("존재하지 않는 초대장입니다.");
        Member receiver = memberReader.getMemberById(rejectInviteRequest.getReceiverId());
        if (receiver == null) throw new IllegalArgumentException("존재하지 않는 멤버입니다.");
        invite.reject(receiver);
        return new InviteInfo(invite);
    }

    @Override
    @Transactional(readOnly = true)
    public InviteInfo retrieveInvite(String uuid) {
        Invite invite = inviteReader.getInviteById(uuid);
        if (invite == null) throw new IllegalArgumentException("존재하지 않는 초대장입니다.");
        return new InviteInfo(invite);
    }
}
