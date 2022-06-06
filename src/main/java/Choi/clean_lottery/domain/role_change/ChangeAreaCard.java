package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.role.area.Area;
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
    private Status state = Status.YET;

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
        if (changer == null && state == Status.YET) {
            changer = member;
            state = Status.IN_PROGRESS;
        }
    }

    public void detachChanger() {
        if (state == Status.IN_PROGRESS) {
            changer = null;
            state = Status.YET;
        }
    }

    public void completeChanging() {
        if (state != Status.IN_PROGRESS) {
            return;
        }
        state = Status.DONE;
        this.changeRoleTable.oneChangeDone();
    }
}
