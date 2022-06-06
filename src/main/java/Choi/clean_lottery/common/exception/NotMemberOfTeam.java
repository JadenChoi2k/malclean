package Choi.clean_lottery.common.exception;

import Choi.clean_lottery.common.response.ErrorCode;

public class NotMemberOfTeam extends BaseException {

    public NotMemberOfTeam() {
        super(ErrorCode.TEAM_INVALID_MEMBER);
    }

    public NotMemberOfTeam(String message) {
        super(message, ErrorCode.TEAM_INVALID_ROLE);
    }
}
