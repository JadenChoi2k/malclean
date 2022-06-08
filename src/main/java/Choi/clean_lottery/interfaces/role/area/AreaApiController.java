package Choi.clean_lottery.interfaces.role.area;

import Choi.clean_lottery.application.role.area.AreaFacade;
import Choi.clean_lottery.common.response.CommonResponse;
import Choi.clean_lottery.domain.role.area.AreaCommand;
import Choi.clean_lottery.domain.role.area.AreaInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/area")
@RequiredArgsConstructor
public class AreaApiController {
    private final AreaFacade areaFacade;
    private final AreaDtoMapper areaDtoMapper;

    @PostMapping("/")
    public CommonResponse register(@RequestBody @Valid AreaDto.RegisterAreaRequest registerAreaRequest) {
        AreaInfo areaInfo = areaFacade.register(areaDtoMapper.of(registerAreaRequest));
        return CommonResponse.success(areaInfo);
    }

    @GetMapping("/{areaId}")
    public CommonResponse retrieve(@PathVariable Long areaId) {
        AreaInfo areaInfo = areaFacade.retrieve(areaId);
        return CommonResponse.success(areaInfo);
    }

    @PutMapping("/{areaId}")
    public CommonResponse edit(
            @PathVariable Long areaId,
            @RequestBody @Valid AreaDto.EditAreaRequest editAreaRequest) {
        AreaInfo areaInfo = areaFacade.edit(combine(areaId, editAreaRequest));
        return CommonResponse.success(areaInfo);
    }

    @DeleteMapping("/{areaId}")
    public CommonResponse detachFromRole(@PathVariable Long areaId) {
        areaFacade.detachFromRole(areaId);
        return CommonResponse.success("ok");
    }

    private AreaCommand.EditAreaRequest combine(
            Long areaId,
            AreaDto.EditAreaRequest editAreaRequest
    ) {
        AreaCommand.EditAreaRequest command = areaDtoMapper.of(editAreaRequest);
        command.setAreaId(areaId);
        return command;
    }
}
