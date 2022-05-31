package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.area.Area;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeAreaCard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "change_area_card_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_area_id")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_table_id")
    private ChangeRoleTable changeRoleTable;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changer_id")
    private Member changer;

    @Enumerated(EnumType.STRING)
    private AreaChangeState state = AreaChangeState.YET;

    @RequiredArgsConstructor
    public enum Status {
        YET("대기중"),
        IN_PROGRESS("진행중"),
        DONE("인수인계완료");
        private final String description;
    }

    public ChangeAreaCard(Area area, ChangeRoleTable changeRoleTable) {
        this.area = area;
        this.changeRoleTable = changeRoleTable;
    }

    public void setChanger(Member member) {
        if (changer == null && state == AreaChangeState.YET) {
            changer = member;
            state = AreaChangeState.IN_PROCESS;
        }
    }

    public void detachChanger() {
        if (state == AreaChangeState.IN_PROCESS) {
            changer = null;
            state = AreaChangeState.YET;
        }
    }

    public void completeChanging() {
        if (state != AreaChangeState.IN_PROCESS) {
            return;
        }
        state = AreaChangeState.DONE;
        this.changeRoleTable.oneChangeDone();
    }
}
