package Choi.clean_lottery.domain.member;

import Choi.clean_lottery.domain.BaseTimeEntity;
import Choi.clean_lottery.domain.team.Team;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
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

    @Setter
    private LocalDateTime lastLoginDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Position position;

    @RequiredArgsConstructor
    @Getter
    public enum Position {
        NONE("팀이없음"),
        TEAM_MANAGER("매니저"),
        TEAM_MEMBER("멤버");

        private final String description;
    }

    public Member(Long id, String name, String profile_url) {
        this.id = id;
        this.name = name;
        this.profile_url = profile_url;
        this.position = Position.NONE;
    }

    public void takeManager() {
        log.info("[Member.takeMember] member {}: take manager in team", getId());
        this.position = Position.TEAM_MANAGER;
    }

    public void takeDownManager() {
        if (this.position == Position.TEAM_MANAGER) {
            log.info("[Member.takeDownManager] member {}: take down manager in team", getId());
            this.position = Position.TEAM_MEMBER;
        }
    }

    public void updateProfile(String name, String profile_url) {
        if (!this.name.equals(name)) {
            this.name = name;
        }
        if (!this.profile_url.equals(profile_url)) {
            this.profile_url = profile_url;
        }
    }

    public boolean hasTeam() {
        return this.team != null;
    }

    public void changeTeam(Team team) {
        if (this.team != null) {
            log.info("[Member.changeTeam] member {}: out of team {}", getId(), getTeam().getId());
            this.team.kickOutMember(this);
            this.position = Position.NONE;
        }

        if (team == null) {
            log.warn("[Member.changeTeam] member {}: dest team is null", getId());
            return;
        }
        this.team = team;
        this.position = Position.TEAM_MEMBER;
        team.getMembers().add(this);
    }

    public void getOutOfTeam() {
        log.info("[Member.getOutOfTeam] member {}: out of team {}", getId(), getTeam().getId());
        // 매니저인 경우.
        if (this.position == Position.TEAM_MANAGER) {
            // 혼자가 아니면 나올 수 없다.
            if (this.team.getMembers().size() != 1) {
                log.info("[Member.getOutOfTeam] member {}: cannot out of team. manager", getId());
                throw new IllegalArgumentException("매니저를 강퇴할 수 없습니다.");
            }
        }
        this.position = Position.NONE;
        this.team = null;
    }
}
