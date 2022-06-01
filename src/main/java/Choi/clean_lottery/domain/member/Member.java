package Choi.clean_lottery.domain.member;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.team.Team;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    // kakao api 호출로 받은 유저의 아이디를 저장한다.
    @Id @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    @Setter
    private String profile_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(Long id, String name, String profile_url) {
        this.id = id;
        this.name = name;
        this.profile_url = profile_url;
    }

    public void changeTeam(Team team) {
        if (this.team != null) {
            this.team.kickOutMember(this);
        }

        this.team = team;
        if (team == null) return;
        team.getMembers().add(this);
    }

    public void getOutOfTeam() {
        this.team = null;
    }
}
