package Choi.clean_lottery.interfaces.team;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class RolesChangeForm {
    @NotEmpty
    private List<Long> roleIds;
    private List<String> roleNames;
    private LocalDate startDate;
    @NotEmpty
    private List<Long> preSequence;

    // 시퀀스를 초기화하는 메서드. 이는 roleEditForms가 올바른 순서로 정렬되어있다고 가정한다.
    public void initializeSequence() {
        preSequence = new ArrayList<>();
        for (Long roleId : roleIds) {
            preSequence.add(roleId);
        }
    }

    public Boolean isSequenceChanged() {
        int size = roleIds.size();
        if (size != preSequence.size()) return null;
        for (int i = 0; i < size; i++) {
            if (roleIds.get(i) != preSequence.get(i)) return true;
        }
        return false;
    }
}
