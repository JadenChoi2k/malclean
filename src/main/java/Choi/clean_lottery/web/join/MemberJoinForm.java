package Choi.clean_lottery.web.join;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class MemberJoinForm {
    @NotBlank
    @Pattern(regexp = "[가-힣]{2,8}", message = "이름은 2~8자의 한글입니다")
    private String name;
    @NotBlank
    @Size(min=6, max=20, message = "아이디는 6~20자입니다.")
    @Pattern(regexp = "[a-zA-Z]+[a-zA-Z0-9]+",
            message = "아이디는 영문, 숫자로 구성되어야 합니다.")
    private String loginId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9~!@#$%^&*()+|=]{8,20}$",
            message = "비밀번호는 숫자, 문자 등을 포함한 8~20자입니다.") // 숫자, 문자, 무조건 1개 이상, 8~20자
    private String password;
}
