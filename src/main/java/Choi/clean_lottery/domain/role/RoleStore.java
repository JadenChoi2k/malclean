package Choi.clean_lottery.domain.role;

public interface RoleStore {
    Role store(Role role);

    void detachFromTeam(Long roleId);
}
