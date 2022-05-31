package Choi.clean_lottery.domain.role_change;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.team.Team;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeRoleTable extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "change_table_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_role_state")
    private Status state = Status.IN_PROGRESS;

    @RequiredArgsConstructor
    public enum Status {
        IN_PROGRESS("인수인계 진행 중"),
        DONE("끝");
        private final String description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_role_id")
    private Role receiveRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "give_role_id")
    private Role giveRole;

    @Column(name = "remaining_number")
    private Integer remainingNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receive_area_card_id")
    private List<ChangeAreaCard> receiveAreaCardList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "give_area_card_id")
    private List<ChangeAreaCard> giveAreaCardList = new ArrayList<>();

    public ChangeRoleTable(Team team, Role receiveRole, Role giveRole) {
        this.team = team;
        this.receiveRole = receiveRole;
        this.giveRole = giveRole;
        receiveRole.getAreas().forEach((a) -> {
            if (a.getChangeable()) {
                this.receiveAreaCardList.add(new ChangeAreaCard(a, this));
            }
        });
        giveRole.getAreas().forEach((a) -> {
            if (a.getChangeable()) {
                this.giveAreaCardList.add(new ChangeAreaCard(a, this));
            }
        });
        this.remainingNumber = (int) getWholeCardSize();
    }

    public long getWholeCardSize() {
        return receiveAreaCardList.size() + giveAreaCardList.size();
    }

    public void oneChangeDone() {
        updateState();
    }

    public boolean isAllDone() {
        for (ChangeAreaCard changeAreaCard : receiveAreaCardList) {
            if (changeAreaCard.getState() != ChangeAreaCard.Status.DONE) {
                return false;
            }
        }
        for (ChangeAreaCard changeAreaCard : giveAreaCardList) {
            if (changeAreaCard.getState() != ChangeAreaCard.Status.DONE) {
                return false;
            }
        }
        return true;
    }

    private void updateState() {
        if (isAllDone()) {
            this.state = Status.DONE;
        }
    }

    public void startChanging() {
        this.team.startRoleChanging();
        this.state = Status.IN_PROGRESS;
    }

    public void endChanging() {
        if (!isAllDone()) {
            return;
        }
        this.state = Status.DONE;
        this.team.updateCurrentRole(receiveRole);
        this.team.endRoleChanging();
    }
}
