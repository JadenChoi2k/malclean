package Choi.clean_lottery.domain.invite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class InviteCommand {

    @Getter
    @Setter
    @Builder
    public static class CreateInviteRequest {
        private Long teamId;
        private Long senderId;
    }

    @Getter
    @Setter
    @Builder
    public static class AcceptInviteRequest {
        private String uuid;
        private Long receiverId;
    }

    @Getter
    @Setter
    @Builder
    public static class RejectInviteRequest {
        private String uuid;
        private Long receiverId;
    }
}
