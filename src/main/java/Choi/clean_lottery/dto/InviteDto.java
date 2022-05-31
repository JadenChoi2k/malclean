package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.invite.Invite;
import lombok.Data;

@Data
public class InviteDto {
    private String uuid;
    private String senderName;
    private String teamName;
    private Invite.Status status;

    public InviteDto() {
    }

    public InviteDto(Invite invite) {
        setUuid(invite.getUuid());
        setSenderName(invite.getSender().getName());
        setTeamName(invite.getTeam().getName());
        setStatus(invite.getStatus());
    }
}
