package Choi.clean_lottery.dto;

import Choi.clean_lottery.domain.member.Member;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String profile_url;
    private String teamName;
    private Boolean isManager;

    public MemberDto(Member member) {
        id = member.getId();
        name = member.getName();
        profile_url = member.getProfile_url();
        if (member.getTeam() != null) {
            teamName = member.getTeam().getName();
            isManager = member.getTeam().getManager() == member;
        }
    }

    public MemberDto(Member member, boolean teamLoad) {
        id = member.getId();
        name = member.getName();
        profile_url = member.getProfile_url();
        if (member.getTeam() != null && teamLoad) {
            teamName = member.getTeam().getName();
            isManager = member.getTeam().getManager() == member;
        } else {
            teamName = null;
            isManager = null;
        }
    }
}
