package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.role_change.AreaChangeState;
import Choi.clean_lottery.domain.role_change.ChangeAreaCard;
import lombok.Data;

@Data
public class ChangeAreaCardDto {
    private Long id;
    private String areaName;
    private MemberDto changer;
    private AreaChangeState state;

    public ChangeAreaCardDto(ChangeAreaCard card) {
        this.id = card.getId();
        this.areaName = card.getArea().getName();
        if (card.getChanger() != null)
            this.changer = new MemberDto(card.getChanger(), false);
        this.state = card.getState();
    }
}
