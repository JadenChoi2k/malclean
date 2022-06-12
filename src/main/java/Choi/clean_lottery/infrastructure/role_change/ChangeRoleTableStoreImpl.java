package Choi.clean_lottery.infrastructure.role_change;

import Choi.clean_lottery.domain.role_change.ChangeRoleTable;
import Choi.clean_lottery.domain.role_change.ChangeRoleTableStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeRoleTableStoreImpl implements ChangeRoleTableStore {
    private final ChangeRoleTableJpaRepository tableRepository;

    @Override
    public ChangeRoleTable store(ChangeRoleTable table) {
        return tableRepository.save(table);
    }
}
