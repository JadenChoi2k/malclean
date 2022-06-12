package Choi.clean_lottery.domain.role;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.area.AreaCommand;
import Choi.clean_lottery.domain.team.Team;
import Choi.clean_lottery.domain.team.TeamReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleReader roleReader;
    private final TeamReader teamReader;
    private final RoleStore roleStore;

    @Override
    @Transactional
    public RoleInfo registerRole(RoleCommand.RegisterRoleRequest createRequest) {
        // get team and create role
        Team team = teamReader.getTeamById(createRequest.getTeamId());
        Role role = new Role(createRequest.getName(), team);
        // add area
        createRequest.getAreaRequestList().stream()
                .map(req -> createAreaByRegisterRequest(role, req))
                .forEach(role::addArea);
        roleStore.store(role);
        return new RoleInfo(role);
    }

    private Area createAreaByRegisterRequest(Role role, AreaCommand.RegisterAreaRequest registerAreaRequest) {
        return new Area(
                role,
                registerAreaRequest.getAreaName(),
                registerAreaRequest.getDifficulty(),
                registerAreaRequest.getMinimumPeople(),
                registerAreaRequest.getChangeable()
        );
    }

    @Override
    @Transactional
    public RoleInfo editRole(RoleCommand.EditRoleRequest editRequest) {
        Role role = roleReader.getRoleById(editRequest.getId());
        role.changeName(editRequest.getName());
        return new RoleInfo(role);
    }

    @Override
    @Transactional
    public void detachFromTeam(Long roleId) {
        Role role = roleReader.getRoleById(roleId);
        role.detachFromTeam();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleInfo retrieveRoleInfo(Long roleId) {
        return new RoleInfo(roleReader.getRoleById(roleId));
    }
}
