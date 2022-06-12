package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.domain.member.MemberInfo;
import Choi.clean_lottery.domain.role.area.AreaInfo;
import Choi.clean_lottery.domain.role_change.card.ChangeAreaCard;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ChangeRoleTableInfo {

    @Getter
    public static class Main {
        private Long tableId;
        private Long teamId;
        private String receiveRoleName;
        private String giveRoleName;
        private List<AreaCardInfo> receiveAreaCardList;
        private List<AreaCardInfo> giveAreaCardList;
        private ChangeRoleTable.Status status;

        public Main(ChangeRoleTable table) {
            this.tableId = table.getId();
            this.teamId = table.getTeam().getId();
            this.receiveRoleName = table.getReceiveRole().getName();
            this.giveRoleName = table.getGiveRole().getName();
            this.receiveAreaCardList = table.getReceiveAreaCardList().stream()
                    .map(AreaCardInfo::new)
                    .collect(Collectors.toList());
            this.giveAreaCardList = table.getGiveAreaCardList().stream()
                    .map(AreaCardInfo::new)
                    .collect(Collectors.toList());
            this.status = table.getState();
        }
    }

    @Getter
    public static class AreaCardInfo {
        private Long cardId;
        private AreaInfo area;
        private MemberInfo member;
        private ChangeAreaCard.Status status;

        public AreaCardInfo(ChangeAreaCard card) {
            this.cardId = card.getId();
            this.area = new AreaInfo(card.getArea());
            this.member = card.getChanger() != null ? new MemberInfo(card.getChanger()) : null;
            this.status = card.getState();
        }
    }
}
