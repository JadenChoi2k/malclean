package Choi.clean_lottery.service;

import Choi.clean_lottery.domain.invite.Invite;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.dto.InviteDto;
import Choi.clean_lottery.repository.InviteRepository;
import Choi.clean_lottery.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteService {

    private final InviteRepository inviteRepository;
    private final MemberRepository memberRepository;

    public Invite findById(String uuid) {
        return inviteRepository.findById(uuid);
    }

    public InviteDto findOneDto(String uuid) {
        Invite invite = findById(uuid);
        return new InviteDto(invite);
    }

    public Invite createInvite(Member sender, Member receiver, Team team) {
        Invite invite = new Invite(UUID.randomUUID().toString(), sender, receiver, team);
        inviteRepository.save(invite);
        return invite;
    }

    public boolean acceptInvite(String uuid, Member receiver) {
        Invite invite = inviteRepository.findById(uuid);
        if (invite == null) return false;
        receiver = memberRepository.merge(receiver);
        invite.setReceiver(receiver);
        boolean result = invite.accept();
        return result;
    }

    public boolean rejectInvite(String uuid, Member receiver) {
        Invite invite = inviteRepository.findById(uuid);
        if (invite == null) return false;
        invite.setReceiver(receiver);
        invite.reject();
        return true;
    }
}
