package Choi.clean_lottery.web.lottery;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class LotteryForm {
    @NotEmpty
    @Size(min = 4, max = 30)
    private String lotteryName;
    @NotEmpty
    private Set<Long> participants;
    @NotNull
    private Long roleId;
    @NotEmpty
    private List<Long> pick;
}
