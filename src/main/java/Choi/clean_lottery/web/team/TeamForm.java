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
}
