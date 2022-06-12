package Choi.clean_lottery.infrastructure.role;

import Choi.clean_lottery.common.exception.EntityNotFoundException;
import Choi.clean_lottery.domain.role.Role;
import Choi.clean_lottery.domain.role.RoleReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleReaderImpl implements RoleReader {
    private final RoleJpaRepository roleRepository;

    @Override
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("역할을 찾을 수 없습니다. roleId: " + roleId));
    }
}
