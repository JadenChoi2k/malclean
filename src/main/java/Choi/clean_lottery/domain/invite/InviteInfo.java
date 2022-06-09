package Choi.clean_lottery.domain.invite;

import lombok.Getter;

@Getter
public class InviteInfo {
    private String uuid;
    private String senderName;
    private String teamName;
    private Invite.Status status;

    public InviteInfo(Invite invite) {
        this.uuid = invite.getUuid();
        this.senderName = invite.getSender().getName();
        this.teamName = invite.getTeam().getName();
        this.status = invite.getStatus();
    }

    public boolean alreadyProcessed() {
        return getStatus() == Invite.Status.ACCEPTED || getStatus() == Invite.Status.REJECTED;
    }
}
