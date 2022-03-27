package Choi.clean_lottery.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private InviteStatus status = InviteStatus.WAITING;

    public enum InviteStatus {
        WAITING ,ACCEPTED, REJECTED
    }

    public Invite(String uuid, Member sender, Member receiver, Team team) {
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
        if (receiver.getTeam() != null || status != InviteStatus.WAITING) {
            status = InviteStatus.REJECTED;
            return false;
        }
        receiver.changeTeam(team);
        status = InviteStatus.ACCEPTED;
        return true;
    }

    public void reject() {
        if (status != InviteStatus.WAITING) {
            return;
        }
        status = InviteStatus.REJECTED;
    }
}
