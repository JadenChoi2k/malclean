package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.ChangeRoleState;
import Choi.clean_lottery.domain.ChangeRoleTable;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChangeRoleTableDto {
    private Long id;
    private ChangeRoleState state;
    private List<ChangeAreaCardDto> givingCardList;
    private List<ChangeAreaCardDto> receivingCardList;

    public ChangeRoleTableDto(ChangeRoleTable table) {
        this.id = table.getId();
        this.state = table.getState();
        this.givingCardList = table.getGiveAreaCardList().stream().map(ChangeAreaCardDto::new).collect(Collectors.toList());
        this.receivingCardList = table.getReceiveAreaCardList().stream().map(ChangeAreaCardDto::new).collect(Collectors.toList());
    }
}
