package Choi.clean_lottery.domain.member;

import lombok.Getter;

@Getter
public class MemberInfo {
    private Long memberId;
    private String memberName;
    private String profileUrl;
    private String teamName;
    private Boolean isManager;

    public MemberInfo(Member member) {
        this.memberId = member.getId();
        this.memberName = member.getName();
        this.profileUrl = member.getProfile_url();
        this.teamName = member.getTeam().getName();
        this.isManager = member.getTeam().getManager().getId().equals(member.getId());
    }
}
