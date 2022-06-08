package Choi.clean_lottery.domain.role;

public interface RoleService {

    RoleInfo registerRole(RoleCommand.RegisterRoleRequest createRequest);

    RoleInfo editRole(RoleCommand.EditRoleRequest editRequest);

    void detachFromTeam(Long roleId);

    RoleInfo retrieveRoleInfo(Long roleId);
}
