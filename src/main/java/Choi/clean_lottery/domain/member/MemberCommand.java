package Choi.clean_lottery.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterMemberRequest {
        private Long id;
        private String name;
        private String profileUrl;

        public Member toEntity() {
            return new Member(id, name, profileUrl);
        }
    }

    @Getter
    @Setter
    @Builder
    public static class UpdateMemberRequest {
        private Long id;
        private String name;
        private String profileUrl;
    }

    @Getter
    @Setter
    @Builder
    public static class ChangeTeamRequest {
        private Long memberId;
        private Long teamId;
    }
}
