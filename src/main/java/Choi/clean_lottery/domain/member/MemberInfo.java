package Choi.clean_lottery.domain.member;

import lombok.Getter;

import java.util.Optional;

@Getter
public class MemberInfo {
    private Long memberId;
    private String memberName;
    private String profileUrl;
    private String teamName;
    private Member.Position position;

    public MemberInfo(Member member) {
        this.memberId = member.getId();
        this.memberName = member.getName();
        this.profileUrl = member.getProfile_url();
        this.teamName = member.hasTeam() ? member.getTeam().getName() : null;
        this.position = member.getPosition();
    }

    public boolean hasTeam() {
        return this.teamName != null;
    }

    public boolean getIsManager() {
        return this.position == Member.Position.TEAM_MANAGER;
    }
}
