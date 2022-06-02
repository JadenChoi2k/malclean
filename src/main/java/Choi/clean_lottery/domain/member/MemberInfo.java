package Choi.clean_lottery.domain.member;

import lombok.Getter;

@Getter
public class MemberInfo {
    private Long id;
    private String name;
    private String profileUrl;
    private String teamName;
    private Boolean isManager;

    public MemberInfo(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.profileUrl = member.getProfile_url();
        this.teamName = member.getTeam().getName();
        this.isManager = member.getTeam().getManager().getId().equals(member.getId());
    }
}
