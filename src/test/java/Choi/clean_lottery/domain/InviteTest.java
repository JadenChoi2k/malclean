package Choi.clean_lottery.domain;

import Choi.clean_lottery.domain.invite.Invite;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InviteTest {

    Invite createInvite() {
        Member sender = new Member(1L, "sender", null, null, null);
        Member receiver = new Member(2L, "sender", null, null, null);
        Team team = new Team("team", sender);
        Invite invite = Invite.create(sender, receiver, team);
        return invite;
    }

    @Test
    public void invite_create() throws Exception {
        // given
        Member sender = new Member(1L, "sender", null, null, null);
        Member receiver = new Member(2L, "sender", null, null, null);
        Team team = new Team("team", sender);
        // when
        Invite invite = Invite.create(sender, receiver, team);
        // then
        assertEquals(sender, invite.getSender());
        assertEquals(receiver, invite.getReceiver());
        assertEquals(team, invite.getTeam());
        assertEquals(invite.getStatus(), Invite.Status.WAITING);
        assertInstanceOf(String.class, invite.getUuid());
    }

    @Test
    public void accept() throws Exception {
        // given
        Invite invite = createInvite();
        // when
        invite.accept();
        // then
        assertEquals(invite.getStatus(), Invite.Status.ACCEPTED);
        assertEquals(invite.getReceiver().getTeam(), invite.getTeam());
    }

    @Test
    public void reject() throws Exception {
        // given
        Invite invite = createInvite();
        // when
        invite.reject();
        // then
        assertEquals(invite.getStatus(), Invite.Status.REJECTED);
        assertNull(invite.getReceiver().getTeam());
    }
}