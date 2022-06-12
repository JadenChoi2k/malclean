package Choi.clean_lottery.interfaces.role_changing;

import Choi.clean_lottery.domain.role_change.card.ChangeAreaCard;
import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ChangeRoleTableDto {

    @Getter
    @Setter
    @Builder
    public static class Main {
        private Long tableId;
        private Long teamId;
        private String receiveRoleName;
        private String giveRoleName;
        private List<AreaCardDto> receiveAreaCardList;
        private List<AreaCardDto> giveAreaCardList;
        private ChangeRoleTable.Status status;
    }

    @Getter
    @Setter
    @Builder
    public static class AreaCardDto {
        private Long cardId;
        private AreaDto area;
        private MemberDto member;
        private ChangeAreaCard.Status status;

        @Getter
        @Setter
        @Builder
        static class AreaDto {
            private Long areaId;
            private String name;
        }

        @Getter
        @Setter
        @Builder
        static class MemberDto {
            private Long memberId;
            private String memberName;
            private String profileUrl;
        }
    }
}


