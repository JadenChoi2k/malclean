package Choi.clean_lottery.domain.role;

public interface RoleService {

    RoleInfo registerRole(RoleCommand.RegisterRoleRequest createRequest);

    RoleInfo editRole(RoleCommand.EditRoleRequest editRequest);

    RoleInfo retrieveRoleInfo(Long roleId);
}
