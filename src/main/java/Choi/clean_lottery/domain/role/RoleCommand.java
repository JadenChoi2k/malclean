package Choi.clean_lottery.domain.role;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

public class RoleCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterRoleRequest {
        @NotNull
        @Length(min = 1, max = 16)
        private String name;
        @NotEmpty
        @Size(min = 1, max = 10)
        private List<@Pattern(regexp = "[a-z가-힣0-9\\- ]{1,15}") String> areaNames;
        @NotEmpty
        @Size(min = 1, max = 10)
        private List<@Max(value = 10) Integer> difficulties;
        @NotEmpty
        @Size(min = 1, max = 10)
        private List<@Max(value = 10) Integer> minimumPeoples;
        @NotEmpty
        @Size(min = 1, max = 10)
        private List<Boolean> changeable;
    }

    /**
     * 현재로서는 이름만 바꾸는 로직.
     */
    @Getter
    @Setter
    @Builder
    public static class EditRoleRequest {
        @NotNull
        private Long id;
        @NotNull
        @Length(min = 1, max = 16)
        private String name;
    }

    /**
     * 예전 역할을 없애지 않고 읽기 전용으로 남겨두는 것을 고려하고 있다.
     */
    @Getter
    @Setter
    @Builder
    public static class DeleteRoleRequest {
        @NotNull
        private Long id;
    }
}
