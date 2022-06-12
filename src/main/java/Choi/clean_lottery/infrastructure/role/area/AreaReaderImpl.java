package Choi.clean_lottery.infrastructure.role.area;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.area.AreaReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AreaReaderImpl implements AreaReader {
    private final AreaJpaRepository areaRepository;

    @Override
    public Area getAreaById(Long areaId) {
        return areaRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("구역을 찾을 수 없습니다. areaId: " + areaId));
    }
}
