package Choi.clean_lottery.infrastructure.role;

import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.role.RoleStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleStoreImpl implements RoleStore {
    private final RoleJpaRepository roleRepository;

    @Override
    public Role store(Role role) {
        return roleRepository.save(role);
    }
}
