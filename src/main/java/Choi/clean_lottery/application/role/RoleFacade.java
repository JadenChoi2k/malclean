package Choi.clean_lottery.application.role;

import Choi.clean_lottery.domain.role.RoleCommand;
import Choi.clean_lottery.domain.role.RoleInfo;
import Choi.clean_lottery.domain.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleFacade {
    private final RoleService roleService;

    public RoleInfo retrieve(Long roleId) {
        return roleService.retrieveRoleInfo(roleId);
    }

    public RoleInfo register(RoleCommand.RegisterRoleRequest registerRoleRequest) {
        return roleService.registerRole(registerRoleRequest);
    }

    public RoleInfo edit(RoleCommand.EditRoleRequest editRoleRequest) {
        return roleService.editRole(editRoleRequest);
    }

    public void detachFromTeam(Long roleId) {
        roleService.detachFromTeam(roleId);
    }
}
