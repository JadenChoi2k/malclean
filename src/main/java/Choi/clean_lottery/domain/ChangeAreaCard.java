package Choi.clean_lottery.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeAreaCard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "change_area_card_id")
    private Long id;

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
