package Choi.clean_lottery.infrastructure.role.area;

import Choi.clean_lottery.domain.role.area.Area;
import Choi.clean_lottery.domain.role.area.AreaStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AreaStoreImpl implements AreaStore {
    private final AreaJpaRepository areaRepository;

    @Override
    public Area store(Area area) {
        return areaRepository.save(area);
    }
}
