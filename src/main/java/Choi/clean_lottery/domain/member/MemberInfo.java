package Choi.clean_lottery.domain.member;

import lombok.Getter;

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
        this.teamName = member.getTeam().getName();
        this.position = member.getPosition();
    }

    public boolean getIsManager() {
        return this.position == Member.Position.TEAM_MANAGER;
    }
}
