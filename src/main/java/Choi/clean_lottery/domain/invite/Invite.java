package Choi.clean_lottery.domain.invite;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.member.Member;
import Choi.clean_lottery.domain.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * 친구에게 보내는 초대장.
 * validation해야 할 것
 * 1. team이 sender의 팀인가
 * 2. receiver의 팀이 없어야 한다.
 * 3. 요청자가 receiver인가
 * 초대장 사용법
 * 초대장 받은 쪽에서 /team/invite/accept/{uuid} 또는 /team/invite/reject/{uuid}
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invite extends BaseTimeEntity {
    @Id
    private String uuid;
    @OneToOne
    @JoinColumn(name = "sender_id")
    private Member sender;
    @OneToOne
    @JoinColumn(name = "receiver_id")
    @Setter
    private Member receiver;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        WAITING("대기중"),
        ACCEPTED("수락됨"),
        REJECTED("거절됨");
        private final String description;
    }

    private Invite(String uuid, Member sender, Member receiver, Team team) {
        this.uuid = uuid;
        this.sender = sender;
        this.receiver = receiver;
        this.team = team;
    }

    public static Invite create(Member sender, Member receiver, Team team) {
        String uuid = UUID.randomUUID().toString();
        return new Invite(uuid, sender, receiver, team);
    }

    public boolean accept() {
        if (receiver.getTeam() != null || status != Status.WAITING) {
            status = Status.REJECTED;
            return false;
        }
        receiver.changeTeam(team);
        status = Status.ACCEPTED;
        return true;
    }

    public void reject() {
        if (status != Status.WAITING) {
            return;
        }
        status = Status.REJECTED;
    }
}
