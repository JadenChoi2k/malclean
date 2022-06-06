package Choi.clean_lottery.common.exception;

import Choi.clean_lottery.common.response.ErrorCode;

public class NotRoleOfTeam extends BaseException {

    public NotRoleOfTeam() {
        super(ErrorCode.TEAM_INVALID_ROLE);
    }

    public NotRoleOfTeam(String message) {
        super(message, ErrorCode.TEAM_INVALID_ROLE);
    }
}
