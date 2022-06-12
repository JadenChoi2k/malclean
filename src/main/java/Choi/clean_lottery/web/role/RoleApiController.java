package Choi.clean_lottery.web.role;

import Choi.clean_lottery.dto.RoleDto;
import Choi.clean_lottery.service.query.RoleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/role/api")
@RequiredArgsConstructor
public class RoleApiController {

    private RoleQueryService roleQueryService;

    @GetMapping("/{roleId}")
    public RoleDto getRole(@PathVariable Long roleId) {
        return roleQueryService.findDto(roleId);
    }
}
