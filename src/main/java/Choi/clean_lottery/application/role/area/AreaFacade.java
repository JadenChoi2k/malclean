package Choi.clean_lottery.application.role.area;

import Choi.clean_lottery.domain.role.area.AreaCommand;
import Choi.clean_lottery.domain.role.area.AreaInfo;
import Choi.clean_lottery.domain.role.area.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AreaFacade {
    private final AreaService areaService;

    public AreaInfo register(AreaCommand.RegisterAreaRequest registerAreaRequest) {
        return areaService.registerArea(registerAreaRequest);
    }

    public AreaInfo edit(AreaCommand.EditAreaRequest editAreaRequest) {
        return areaService.editArea(editAreaRequest);
    }

    public AreaInfo retrieve(Long areaId) {
        return areaService.retrieve(areaId);
    }

    public void detachFromRole(Long areaId) {
        areaService.detachFromRole(areaId);
    }
}
