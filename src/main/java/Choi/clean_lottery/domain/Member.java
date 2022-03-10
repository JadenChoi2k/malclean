package Choi.clean_lottery.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    @Setter
    private String profile_url;

    // 마지막 로그인 일시
    @Setter
    private LocalDateTime lastLoginDateTime;
    // 가입 일시
    @Column(updatable = false)
    private LocalDateTime joinDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(Long id, String name, String profile_url, LocalDateTime lastLoginDateTime, LocalDateTime joinDateTime) {
        this.id = id;
        this.name = name;
        this.profile_url = profile_url;
        this.lastLoginDateTime = lastLoginDateTime;
        this.joinDateTime = joinDateTime;
    }

    public void changeTeam(Team team) {
        if (this.team != null) {
            this.team.kickOutMember(this);
        }

        this.team = team;
        team.getMembers().add(this);
    }

    public void getOutOfTeam() {
        this.team = null;
    }
}
