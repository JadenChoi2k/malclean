package Choi.clean_lottery.domain.role_change.card;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ChangeAreaCardCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterChangerRequest {
        private Long memberId;
        private Long areaCardId;
    }

    @Getter
    @Setter
    @Builder
    public static class DetachChangerRequest {
        private Long memberId;
        private Long areaCardId;
    }
}
