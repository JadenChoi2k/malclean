package Choi.clean_lottery.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("내부 오류 발생. 잠시 후 다시 시도해주세요. 해당 증상이 반복되면 개발자에게 연락해주세요.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
