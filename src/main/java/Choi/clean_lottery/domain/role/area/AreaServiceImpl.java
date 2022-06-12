package Choi.clean_lottery.domain.role.area;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.role.RoleReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaStore areaStore;
    private final AreaReader areaReader;
    private final RoleReader roleReader;

    @Override
    @Transactional
    public AreaInfo registerArea(AreaCommand.RegisterAreaRequest registerAreaRequest) {
        Role role = roleReader.getRoleById(registerAreaRequest.getRoleId());
        Area area = registerAreaRequest.toEntity(role);
        areaStore.store(area);
        return new AreaInfo(area);
    }

    @Override
    @Transactional
    public AreaInfo editArea(AreaCommand.EditAreaRequest editAreaRequest) {
        Area area = areaReader.getAreaById(editAreaRequest.getAreaId());
        area.changeAttribute(
                editAreaRequest.getAreaName(),
                editAreaRequest.getDifficulty(),
                editAreaRequest.getMinimumPeople(),
                editAreaRequest.getChangeable()
        );
        return new AreaInfo(area);
    }

    @Override
    @Transactional
    public void detachFromRole(Long areaId) {
        Area area = areaReader.getAreaById(areaId);
        area.detachFromRole();
    }

    @Override
    @Transactional(readOnly = true)
    public AreaInfo retrieve(Long areaId) {
        Area area = areaReader.getAreaById(areaId);
        if (area == null) {
            log.info("EntityNotFound : areaId={}", areaId);
            throw new EntityNotFoundException("역할을 찾을 수 없습니다.");
        }
        return new AreaInfo(area);
    }
}
