package Choi.clean_lottery.domain.role_change;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ChangeRoleTableCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterTableRequest {
        private Long teamId;
        private Long giveRoleId;
        private Long receiveRoleId;
    }
}
