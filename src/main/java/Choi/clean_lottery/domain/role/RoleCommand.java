package Choi.clean_lottery.domain.role;

import Choi.clean_lottery.domain.role.area.AreaCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RoleCommand {

    @Getter
    @Setter
    @Builder
    public static class RegisterRoleRequest {
        private Long teamId;
        private String name;
        private List<AreaCommand.RegisterAreaRequest> areaRequestList;
    }

    /**
     * 현재로서는 이름만 바꾸는 로직.
     */
    @Getter
    @Setter
    @Builder
    public static class EditRoleRequest {
        private Long id;
        private String name;
    }

    /**
     * 예전 역할을 없애지 않고 읽기 전용으로 남겨두는 것을 고려하고 있다.
     */
    @Getter
    @Setter
    @Builder
    public static class DeleteRoleRequest {
        private Long id;
    }
}
