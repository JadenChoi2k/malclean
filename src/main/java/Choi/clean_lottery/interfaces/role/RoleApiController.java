package Choi.clean_lottery.interfaces.role;

import Choi.clean_lottery.application.role.RoleFacade;
import Choi.clean_lottery.common.response.CommonResponse;
import Choi.clean_lottery.domain.role.RoleCommand;
import Choi.clean_lottery.domain.role.RoleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleApiController {
    private final RoleFacade roleFacade;
    private final RoleDtoMapper roleDtoMapper;

    @GetMapping("/{roleId}")
    public CommonResponse retrieve(@PathVariable Long roleId) {
        return CommonResponse.success(roleFacade.retrieve(roleId));
    }

    @PostMapping("/")
    public CommonResponse register(@RequestBody @Valid RoleDto.RegisterRoleRequest registerRoleRequest) {
        RoleCommand.RegisterRoleRequest command = roleDtoMapper.of(registerRoleRequest);
        RoleInfo roleInfo = roleFacade.register(command);
        return CommonResponse.success(roleInfo);
    }

    @PutMapping("/{roleId}")
    public CommonResponse edit(
            @PathVariable Long roleId,
            @RequestBody @Valid RoleDto.EditRoleRequest editRoleRequest) {
        RoleCommand.EditRoleRequest command = combine(roleId, editRoleRequest);
        RoleInfo roleInfo = roleFacade.edit(command);
        return CommonResponse.success(roleInfo);
    }

    private RoleCommand.EditRoleRequest combine(Long roleId, RoleDto.EditRoleRequest editRoleRequest) {
        RoleCommand.EditRoleRequest command = roleDtoMapper.of(editRoleRequest);
        command.setId(roleId);
        return command;
    }

    @DeleteMapping("/{roleId}")
    public CommonResponse detachFromTeam(@PathVariable Long roleId) {
        roleFacade.detachFromTeam(roleId);
        return CommonResponse.success("ok");
    }
}
