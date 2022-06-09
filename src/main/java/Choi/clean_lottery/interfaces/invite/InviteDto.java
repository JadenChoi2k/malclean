package Choi.clean_lottery.interfaces.invite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InviteDto {
    private String uuid;
    private String senderName;
    private String teamName;
    private String status;
}
