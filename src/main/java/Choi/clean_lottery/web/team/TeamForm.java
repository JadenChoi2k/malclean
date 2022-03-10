package Choi.clean_lottery.web.team;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;

// 팀 생성 폼
// 팀 이름, 초기 멤버, 인수인계 주기, 인수인계 요일, 인수인계 d-day(현재 기준), 청소 역할.
@Getter @Setter
public class TeamForm {
    @Pattern(regexp = "[a-zA-Z가-힣0-9 ]{2,16}")
    private String teamName;
    // 처음에 초대할 멤버의 id 목록. 존재하는 팀이 없어야 초대 가능하다.
    // 한 사람 당 하나의 팀만 있기 때문에...!
    private List<Long> memberIds;
}
